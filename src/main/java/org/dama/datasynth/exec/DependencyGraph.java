package org.dama.datasynth.exec;

import org.dama.datasynth.common.Types;
import org.dama.datasynth.lang.Ast;
import org.jgrapht.graph.DirectedMultigraph;

import java.util.*;

/**
 * Created by quim on 5/12/16.
 */
public class DependencyGraph  extends DirectedMultigraph<Vertex,DEdge> {

    private List<Vertex> entryPoints = new ArrayList<Vertex>();

    /**
     * Constructor
     * @param ast The ast to build the dependency graph from
     * @throws Exception
     */
    public DependencyGraph(Ast ast) {
        super((v1, v2) -> new DEdge(v1, v2));
        initialize(ast);
    }


    /**
     * Initializes the dependency graph given an Ast
     * @param ast The Ast to initialize the dependency graph from
     */
    private void initialize(Ast ast) {
        //############################################################################
        Map<String,Attribute> tasks = new TreeMap<String,Attribute>();
        for(Ast.Entity entity : ast.getEntities()) {
            Vertex entityTask = new Entity(entity.getName());
            entryPoints.add(entityTask);
            //g.addVertex(entityTask);
            Attribute oid = new Attribute(entity,new Ast.Attribute("oid", Types.DATATYPE.INTEGER, new Ast.Generator("IdGenerator")));
            tasks.put(entity.getName()+".oid", oid);
            addVertex(oid);
            addVertex(entityTask);
            addEdge(oid,entityTask);
            for(Ast.Attribute attribute : entity.getAttributes()) {
                Attribute task = new Attribute(entity,attribute);
                tasks.put(task.getEntity().getName()+"."+task.getAttributeName(),task);
                addVertex(task);
                addEdge(oid,task);
                addEdge(task, entityTask);
            }
        }
        //############################################################################

        Set<Attribute> processed = new TreeSet<Attribute>((t1, t2) -> { return t1.toString().compareTo(t2.toString());});
        for(Map.Entry<String,Attribute> task : tasks.entrySet() ) {
            if(task.getKey().substring(task.getKey().length()-3, task.getKey().length()).equalsIgnoreCase("oid")){
                processed.add(task.getValue());
            }else if( !processed.contains(task.getValue())) {
                List<Attribute> toProcess = new LinkedList<Attribute>();
                toProcess.add(task.getValue());
                while(!toProcess.isEmpty()) {
                    Attribute currentTask = toProcess.get(0);
                    toProcess.remove(0);
                    processed.add(currentTask);
                    for (String param : currentTask.getRunParameters()) {
                        Attribute otherTask = tasks.get(currentTask.getEntity().getName()+"."+param);
                        if (otherTask != null) {
                            if (!processed.contains(otherTask)) {
                                toProcess.add(otherTask);
                            }
                            addEdge(otherTask,currentTask);
                        }
                    }
                }
            }
        }
        System.out.println("Processed " + processed.size() + " Tasks " + tasks.size());
        if(processed.size() != tasks.size()) throw new RuntimeException("Critical internal Error. Dependency plan wrongly built. Some nodes might be missing");
        //Now process the edges which represent relationships between different entities

        Map<String, Entity> entities = new HashMap<>();
        for(Vertex vtx : entryPoints) {
            entities.put(vtx.getId(), (Entity) vtx);
        }

        for(Ast.Edge edge : ast.getEdges()) {
            Entity source = entities.get(edge.getOrigin().getName());
            Entity target = entities.get(edge.getDestination().getName());
            ArrayList<Attribute> sourceAttributes = new ArrayList<Attribute>();
            ArrayList<Attribute> targetAttributes = new ArrayList<Attribute>();
            String orig = edge.getOrigin().getName();
            String dest = edge.getDestination().getName();
            for(Ast.Attribute attr : edge.getOrigin().getAttributes()) sourceAttributes.add(tasks.get(orig+"."+attr.getName()));
            for(Ast.Attribute attr : edge.getDestination().getAttributes()) targetAttributes.add(tasks.get(dest+"."+attr.getName()));
            Edge et = new Edge(edge,source, target, sourceAttributes, targetAttributes);
            addVertex(et);
            addEdge(source,et);
            addEdge(target,et);
        }
    }

    public List<Vertex> getEntryPoints() {
        return entryPoints;
    }

/**
     * Prints the dependency graph
     */
    /*public void print(){
        for(DEdge e: edgeSet()){
            System.out.println(e.getSource().getType() + " :: " + e.getTarget().getType());
            System.out.println(e.getSource().getName() + " <- " + e.getTarget().getName());
        }
    }*/
}
