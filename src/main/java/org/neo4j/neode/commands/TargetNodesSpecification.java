package org.neo4j.neode.commands;

import static org.neo4j.neode.numbergenerators.ProbabilityDistribution.flatDistribution;
import static org.neo4j.neode.numbergenerators.ProbabilityDistribution.normalDistribution;

import java.util.List;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.neode.numbergenerators.ProbabilityDistribution;

public class TargetNodesSpecification
{
    public static NodeFinder getExisting( NodeCollection nodeCollection, ProbabilityDistribution
            probabilityDistribution )
    {
        return new ExistingUniqueNodeFinder( nodeCollection, probabilityDistribution );
    }

    public static NodeFinder getExisting( NodeCollection nodeCollection )
    {
        return new ExistingUniqueNodeFinder( nodeCollection, normalDistribution() );
    }

    public static NodeFinder queryBasedGetOrCreate( NodeSpecification nodeSpecification, GraphQuery graphQuery )
    {
        return new QueryBasedGetOrCreate( nodeSpecification,
                new SparseNodeListGenerator( graphQuery, 1.0, flatDistribution() ) );
    }

    public static NodeFinder queryBasedGetOrCreate( NodeSpecification nodeSpecification, GraphQuery graphQuery,
                                                    double proportionOfCandidateNodesToRequiredNodes )
    {
        return new QueryBasedGetOrCreate( nodeSpecification,
                new SparseNodeListGenerator( graphQuery, proportionOfCandidateNodesToRequiredNodes,
                        flatDistribution() ) );
    }

    public static NodeFinder getOrCreate( NodeSpecification nodeSpecification, int maxNumberOfEntities,
                                          ProbabilityDistribution probabilityDistribution )
    {
        return new GetOrCreateUniqueNodeFinder( nodeSpecification, maxNumberOfEntities, probabilityDistribution );
    }

    public static NodeFinder getOrCreate( NodeSpecification nodeSpecification, int maxNumberOfEntities )
    {
        return new GetOrCreateUniqueNodeFinder( nodeSpecification, maxNumberOfEntities, flatDistribution() );
    }

    private final NodeFinder nodeFinder;
    private final RelationshipInfo relationshipInfo;
    private final RelationshipConstraints relationshipConstraints;

    TargetNodesSpecification( NodeFinder nodeFinder, RelationshipInfo relationshipInfo,
                              RelationshipConstraints relationshipConstraints )
    {
        this.nodeFinder = nodeFinder;
        this.relationshipInfo = relationshipInfo;
        this.relationshipConstraints = relationshipConstraints;
    }

    int addRelationshipsToCurrentNode( GraphDatabaseService db, Node currentNode,
                                       NodeIdCollector targetNodeIdCollector, Random random )
    {
        int count = 0;
        Iterable<Node> targetNodes = getRandomSelectionOfNodes( db, currentNode, random );
        for ( Node targetNode : targetNodes )
        {
            Relationship relationship = relationshipConstraints
                    .addRelationshipToCurrentNode( currentNode, targetNode, db, targetNodeIdCollector,
                            relationshipInfo, random );
            if ( relationship != null )
            {
                count++;
            }
        }
        return count;
    }

    NodeCollection newNodeCollection( List<Long> nodeIds )
    {
        return new NodeCollection( nodeFinder.label(), nodeIds );
    }

    String createRelationshipDescription( String startNodeLabel )
    {
        return String.format( "(%s)%s(%s)",
                startNodeLabel, relationshipInfo.description(), nodeFinder.label() );
    }

    String createRelationshipConstraintsDescription()
    {
        return relationshipConstraints.description();
    }

    private Iterable<Node> getRandomSelectionOfNodes( GraphDatabaseService db, Node firstNode, Random random )
    {
        int numberOfRelsToCreate = relationshipConstraints.calculateNumberOfRelsToCreate( random );
        return nodeFinder.getNodes( numberOfRelsToCreate, db, firstNode, random );
    }

}