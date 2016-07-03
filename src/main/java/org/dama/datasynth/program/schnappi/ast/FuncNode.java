package org.dama.datasynth.program.schnappi.ast;

import java.util.ArrayList;

/**
 * Created by quim on 5/17/16.
 */
public class FuncNode extends Node {
    //public ArrayList<String> ids;
    public FuncNode(String id, String id1, String id2){
        this(id);
        this.addChild(new ParamsNode(id1, id2));
    }
    public FuncNode(String id){
        super(id);
    }
    /*@Override
    public String toString(){
        String str = "<" + this.id + " ";
        str = str + params;
        str += ">";
        return str;
    }*/
    @Override
    public Node copy(){
        FuncNode fn = new FuncNode(this.id);
        for(Node child : this.children){
            fn.addChild(child.copy());
        }
        return fn;
    }
}