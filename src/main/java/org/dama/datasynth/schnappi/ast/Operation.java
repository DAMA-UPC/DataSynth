package org.dama.datasynth.schnappi.ast;

/**
 * Created by aprat on 22/08/16.
 * Abstract class representing an operation in the Schnappi Ast.
 */
public abstract class Operation extends Node {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);

    }

    @Override
    public abstract Operation copy();
}
