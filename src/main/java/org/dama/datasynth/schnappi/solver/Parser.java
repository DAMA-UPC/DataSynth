package org.dama.datasynth.schnappi.solver;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.dama.datasynth.schnappi.SchnappiGeneratorVisitor;
import org.dama.datasynth.schnappi.SchnappiLexer;
import org.dama.datasynth.schnappi.SchnappiParser;

import java.io.IOException;

/**
 * Created by quim on 5/5/16.
 * Parser for the schnappi solvers
 */
public class Parser {
    /**
     * Parses a solver file
     * @param file The filename to parse
     * @return The solver
     */
    public static Solver parse( String file ){
        SchnappiLexer SchLexer = null;
        try {
            SchLexer = new SchnappiLexer( new ANTLRFileStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CommonTokenStream tokens = new CommonTokenStream( SchLexer );
        SchnappiParser SchParser = new SchnappiParser( tokens );
        SchnappiParser.SolverContext sctx = SchParser.solver();
        SchnappiGeneratorVisitor visitor = new SchnappiGeneratorVisitor();
        return visitor.visitSolver(sctx);
    }
}
