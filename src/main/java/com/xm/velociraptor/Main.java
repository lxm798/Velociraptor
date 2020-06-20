package com.xm.velociraptor;

import antlr4.VelociraptorLexer;
import antlr4.VelociraptorParser;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.nio.charset.Charset;

public class Main {
    public static void main(String[] args) throws IOException {
        Charset charset = Charset.forName("UTF-8");

        ANTLRFileStream antlrFileStream = new ANTLRFileStream("D:\\testcase\\express", "UTF-8");
        //ANTLRInputStream antlrInputStream = new ANTLRInputStream(System.in);
        VelociraptorLexer lexer = new VelociraptorLexer(antlrFileStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
        commonTokenStream.fill();

        VelociraptorParser parser = new VelociraptorParser(commonTokenStream);

/*        ParseTreeWalker walker = new ParseTreeWalker();
/*        ParseTreeWalker walker = new ParseTreeWalker();
/*        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new TransaArrayInitBaseListener(), parseTree);
        System.out.println();*/
        commonTokenStream.reset();
        VelociraptorParserVisitorImpl visitor = new VelociraptorParserVisitorImpl();

        VelociraptorValue value = visitor.visitProgram(parser.program());
        System.out.println(value.getType().name() + "  " +value.getValue());
    }
}
