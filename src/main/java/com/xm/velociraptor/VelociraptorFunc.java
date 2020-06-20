package com.xm.velociraptor;

import antlr4.VelociraptorParser;
import com.xm.velociraptor.enums.TypeEnums;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class VelociraptorFunc extends VelociraptorValue<VelociraptorParser.BlockContext> {
    private List<String> idList = new ArrayList<String>();
    private VelociraptorParserVisitorImpl visitor;

    @Override
    protected void assertTypeCompatible(TypeEnums type) {
        return;
    }

    public List<String> getIdList() {
        return idList;
    }
    public VelociraptorFunc(VelociraptorParserVisitorImpl velociraptorParserVisitor,
                            VelociraptorParser.BlockContext context,
        VelociraptorParser.IdentifierListContext idListContext) {
        super(TypeEnums.Function);
        visitor = velociraptorParserVisitor;
        setValue(context);
        for (TerminalNode node :  idListContext.IDENTIFIER()) {
            idList.add(node.getText());
        }
    }


    public VelociraptorValue call() {
        VelociraptorValue ret = visitor.visitBlock(getValue());
        return ret;
    }

}
