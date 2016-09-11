package org.dama.datasynth.schnappi;


import org.dama.datasynth.schnappi.ast.Ast;
import org.dama.datasynth.schnappi.ast.Atomic;
import org.dama.datasynth.schnappi.ast.Number;
import org.dama.datasynth.schnappi.ast.Operation;
import org.dama.datasynth.schnappi.solver.Solver;

/**
 * Created by quim on 5/17/16.
 */
public class SchnappiGeneratorVisitor extends org.dama.datasynth.schnappi.SchnappiParserBaseVisitor<org.dama.datasynth.schnappi.ast.Node> {

    @Override
    public Solver visitSolver(org.dama.datasynth.schnappi.SchnappiParser.SolverContext ctx){


        org.dama.datasynth.schnappi.ast.Signature signature = visitSignature(ctx.signature());
        Ast ast = new Ast();
        for(org.dama.datasynth.schnappi.SchnappiParser.OpContext op :  ctx.program().op()) {
           ast.addStatement(visitOp(op));

        }
        return new Solver(signature,ast);
    }

    @Override
    public org.dama.datasynth.schnappi.ast.Signature visitSignature(org.dama.datasynth.schnappi.SchnappiParser.SignatureContext ctx){
        return new org.dama.datasynth.schnappi.ast.Signature(ctx.source().VTYPE().getText(), ctx.target().VTYPE().getText());
    }

    @Override
    public Operation visitOp(org.dama.datasynth.schnappi.SchnappiParser.OpContext ctx){
        if(ctx.assig() != null)  return visitAssig(ctx.assig());
        return null;
    }

    @Override
    public org.dama.datasynth.schnappi.ast.Function visitInit(org.dama.datasynth.schnappi.SchnappiParser.InitContext ctx){
        org.dama.datasynth.schnappi.ast.Parameters parameters = new org.dama.datasynth.schnappi.ast.Parameters();
        Atomic atomic = null;
        if(ctx.BINDING() != null) atomic = new org.dama.datasynth.schnappi.ast.Binding(ctx.BINDING().getText());
        if(ctx.ID() != null) atomic = new org.dama.datasynth.schnappi.ast.StringLiteral(ctx.ID().getText());
        parameters.addParam(atomic);
        parameters.mergeParams(visitParams(ctx.params()));
        org.dama.datasynth.schnappi.ast.Function function= new org.dama.datasynth.schnappi.ast.Function("init", parameters);
        return function;
    }

    @Override
    public org.dama.datasynth.schnappi.ast.Assign visitAssig(org.dama.datasynth.schnappi.SchnappiParser.AssigContext ctx) {

        Atomic atomic = null;
        if(ctx.BINDING() != null) atomic = new org.dama.datasynth.schnappi.ast.Binding(ctx.BINDING().getText());
        if(ctx.ID() != null) atomic = new org.dama.datasynth.schnappi.ast.Id(ctx.ID().getText());
        return new org.dama.datasynth.schnappi.ast.Assign(atomic, visitExpr(ctx.expr()));
    }

    @Override
    public org.dama.datasynth.schnappi.ast.Expression visitExpr(org.dama.datasynth.schnappi.SchnappiParser.ExprContext ctx){
        if(ctx.funcs() != null) {
            return visitFuncs(ctx.funcs());
        } else if(ctx.any() != null) {
            return visitAny(ctx.any());
        }
        return null;
    }

    @Override
    public Atomic visitAny(org.dama.datasynth.schnappi.SchnappiParser.AnyContext ctx) {
        if(ctx.NUM() != null)  return new Number(ctx.getText());
        if(ctx.BINDING() != null)  return new org.dama.datasynth.schnappi.ast.Binding(ctx.getText());
        if(ctx.STRING() != null)  return new org.dama.datasynth.schnappi.ast.StringLiteral(ctx.getText());
        if(ctx.ID() != null)  return new org.dama.datasynth.schnappi.ast.Id(ctx.getText());
        return null;
    }


    @Override
    public org.dama.datasynth.schnappi.ast.Expression visitFuncs(org.dama.datasynth.schnappi.SchnappiParser.FuncsContext ctx){
        if(ctx.init() != null) return visitInit(ctx.init());
        else if(ctx.genids() != null) return visitGenids(ctx.genids());
        else if(ctx.map() != null) return visitMap(ctx.map());
        else if(ctx.reduce() != null) return visitReduce(ctx.reduce());
        else if(ctx.union() != null) return visitUnion(ctx.union());
        else if(ctx.eqjoin() != null) return visitEqjoin(ctx.eqjoin());
        return null;
    }

    @Override
    public org.dama.datasynth.schnappi.ast.Function visitMap(org.dama.datasynth.schnappi.SchnappiParser.MapContext ctx) {
        org.dama.datasynth.schnappi.ast.Parameters parameters = new org.dama.datasynth.schnappi.ast.Parameters();
        for(org.dama.datasynth.schnappi.SchnappiParser.AnyContext tn : ctx.any()) {
                parameters.addParam(visitAny(tn));
        }
        return new org.dama.datasynth.schnappi.ast.Function("map",parameters);
    }

    @Override
    public org.dama.datasynth.schnappi.ast.Function visitUnion(org.dama.datasynth.schnappi.SchnappiParser.UnionContext ctx){
        return new org.dama.datasynth.schnappi.ast.Function("union",(visitParams(ctx.params())));
    }

    @Override
    public org.dama.datasynth.schnappi.ast.Function visitReduce(org.dama.datasynth.schnappi.SchnappiParser.ReduceContext ctx) {
        org.dama.datasynth.schnappi.ast.Parameters parameters = new org.dama.datasynth.schnappi.ast.Parameters();
        for(org.dama.datasynth.schnappi.SchnappiParser.AnyContext tn : ctx.any()) {
                parameters.addParam(visitAny(tn));
        }
        return new org.dama.datasynth.schnappi.ast.Function("reduce",parameters);
    }

    @Override
    public org.dama.datasynth.schnappi.ast.Function visitEqjoin(org.dama.datasynth.schnappi.SchnappiParser.EqjoinContext ctx){
        return new org.dama.datasynth.schnappi.ast.Function("eqjoin",visitParams(ctx.params()));
    }

    @Override
    public org.dama.datasynth.schnappi.ast.Parameters visitParams(org.dama.datasynth.schnappi.SchnappiParser.ParamsContext ctx){
        org.dama.datasynth.schnappi.ast.Parameters parameters = new org.dama.datasynth.schnappi.ast.Parameters();
        for( org.dama.datasynth.schnappi.SchnappiParser.AnyContext tn : ctx.any()){
            Atomic atomic = null;
            if(tn.NUM() != null) atomic = new Number(tn.getText());
            if(tn.STRING() != null) atomic = new org.dama.datasynth.schnappi.ast.StringLiteral(tn.getText());
            if(tn.BINDING() != null) atomic = new org.dama.datasynth.schnappi.ast.Binding(tn.getText());
            parameters.addParam(atomic);
        }
        return parameters;
    }

    @Override
    public org.dama.datasynth.schnappi.ast.Function visitGenids(org.dama.datasynth.schnappi.SchnappiParser.GenidsContext ctx){
        org.dama.datasynth.schnappi.ast.Parameters parameters = new org.dama.datasynth.schnappi.ast.Parameters();
        parameters.addParam(new org.dama.datasynth.schnappi.ast.Literal(ctx.NUM().getSymbol().getText()));
        return new org.dama.datasynth.schnappi.ast.Function("geinds",parameters);
    }
    @Override
    public org.dama.datasynth.schnappi.ast.Function visitSort(org.dama.datasynth.schnappi.SchnappiParser.SortContext ctx){
        return new org.dama.datasynth.schnappi.ast.Function("sort",(visitParams(ctx.params())));
    }
    @Override
    public org.dama.datasynth.schnappi.ast.Function visitPartition(org.dama.datasynth.schnappi.SchnappiParser.PartitionContext ctx){
        return new org.dama.datasynth.schnappi.ast.Function("partition",(visitParams(ctx.params())));
    }
    @Override
    public org.dama.datasynth.schnappi.ast.Function visitMappart(org.dama.datasynth.schnappi.SchnappiParser.MappartContext ctx){
        org.dama.datasynth.schnappi.ast.Parameters parameters = new org.dama.datasynth.schnappi.ast.Parameters();
        for(org.dama.datasynth.schnappi.SchnappiParser.AnyContext tn : ctx.any()) {
            parameters.addParam(visitAny(tn));
        }
        return new org.dama.datasynth.schnappi.ast.Function("mappart",parameters);
    }
}