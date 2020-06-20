package com.xm.velociraptor;

import antlr4.VelociraptorLexer;
import antlr4.VelociraptorParser;
import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DiagnosticErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionMode;

import javax.print.PrintException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestRigCp {

    public static final String LEXER_START_RULE_NAME = "tokens";
    protected String grammarName;
    protected String startRuleName;
    protected final List<String> inputFiles = new ArrayList();
    protected boolean printTree = false;
    protected boolean gui = false;
    protected String psFile = null;
    protected boolean showTokens = false;
    protected boolean trace = false;
    protected boolean diagnostics = false;
    protected String encoding = null;
    protected boolean SLL = false;

    public TestRigCp(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("java org.antlr.v4.gui.TestRig GrammarName startRuleName\n  [-tokens] [-tree] [-gui] [-ps file.ps] [-encoding encodingname]\n  [-trace] [-diagnostics] [-SLL]\n  [input-filename(s)]");
            System.err.println("Use startRuleName='tokens' if GrammarName is a lexer grammar.");
            System.err.println("Omitting input-filename makes rig read from stdin.");
        } else {
            int i = 0;
            this.grammarName = args[i];
            i = i + 1;
            this.startRuleName = args[i];
            ++i;

            while (i < args.length) {
                String arg = args[i];
                ++i;
                if (arg.charAt(0) != '-') {
                    this.inputFiles.add(arg);
                } else {
                    if (arg.equals("-tree")) {
                        this.printTree = true;
                    }

                    if (arg.equals("-gui")) {
                        this.gui = true;
                    }

                    if (arg.equals("-tokens")) {
                        this.showTokens = true;
                    } else if (arg.equals("-trace")) {
                        this.trace = true;
                    } else if (arg.equals("-SLL")) {
                        this.SLL = true;
                    } else if (arg.equals("-diagnostics")) {
                        this.diagnostics = true;
                    } else if (arg.equals("-encoding")) {
                        if (i >= args.length) {
                            System.err.println("missing encoding on -encoding");
                            return;
                        }

                        this.encoding = args[i];
                        ++i;
                    } else if (arg.equals("-ps")) {
                        if (i >= args.length) {
                            System.err.println("missing filename on -ps");
                            return;
                        }

                        this.psFile = args[i];
                        ++i;
                    }
                }
            }

        }
    }

    public static void main(String[] args) throws Exception {
        File directory = new File(".");
        System.out.println(directory.getCanonicalPath());
        System.out.println(directory.getAbsolutePath());
        TestRigCp testRig = new TestRigCp(args);
        if (args.length >= 2) {
            testRig.process();
        }

    }

    public void process() throws Exception {
        String lexerName = this.grammarName + "Lexer";


        Lexer lexer = new VelociraptorLexer(null);
        Parser parser =  new VelociraptorParser(null);
/*        if (!this.startRuleName.equals("tokens")) {
            String parserName = this.grammarName + "Parser";
            parserClass = cl.loadClass(parserName).asSubclass(Parser.class);
            Constructor<? extends Parser> parserCtor = parserClass.getConstructor(TokenStream.class);
            parser = (Parser) parserCtor.newInstance((TokenStream) null);
        }*/

        Charset charset = this.encoding == null ? Charset.defaultCharset() : Charset.forName(this.encoding);
        if (this.inputFiles.size() == 0) {
            CharStream charStream = CharStreams.fromStream(System.in, charset);
            this.process(lexer, parser.getClass(), parser, charStream);
        } else {
            CharStream charStream;
            for (Iterator i$ = this.inputFiles.iterator(); i$.hasNext(); this.process(lexer, parser.getClass(), parser, charStream)) {
                String inputFile = (String) i$.next();
                charStream = CharStreams.fromPath(Paths.get(inputFile), charset);
                if (this.inputFiles.size() > 1) {
                    System.err.println(inputFile);
                }
            }

        }
    }

    protected void process(Lexer lexer, Class<? extends Parser> parserClass, Parser parser, CharStream input) throws IOException, IllegalAccessException, InvocationTargetException, PrintException {
        lexer.setInputStream(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        if (this.showTokens) {
            Iterator i$ = tokens.getTokens().iterator();

            while (i$.hasNext()) {
                Token tok = (Token) i$.next();
                if (tok instanceof CommonToken) {
                    System.out.println(((CommonToken) tok).toString(lexer));
                } else {
                    System.out.println(tok.toString());
                }
            }
        }

        if (!this.startRuleName.equals("tokens")) {
            if (this.diagnostics) {
                parser.addErrorListener(new DiagnosticErrorListener());
                ((ParserATNSimulator) parser.getInterpreter()).setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
            }

            if (this.printTree || this.gui || this.psFile != null) {
                parser.setBuildParseTree(true);
            }

            if (this.SLL) {
                ((ParserATNSimulator) parser.getInterpreter()).setPredictionMode(PredictionMode.SLL);
            }

            parser.setTokenStream(tokens);
            parser.setTrace(this.trace);

            try {
                Method startRule = parser.getClass().getMethod(this.startRuleName);
                ParserRuleContext tree = (ParserRuleContext) startRule.invoke(parser, (Object[]) null);
                System.out.println(tree.toStringTree(parser));

                if (this.gui) {
                    Trees.inspect(tree, parser);
                }

                if (this.psFile != null) {
                    Trees.save(tree, parser, this.psFile);
                }
            } catch (NoSuchMethodException var8) {
                var8.printStackTrace();
                System.err.println("No method for rule " + this.startRuleName + " or it has arguments");
            }

        }
    }
}
