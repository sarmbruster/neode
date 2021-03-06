package org.neo4j.neode;

import static java.util.Arrays.asList;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.neode.logging.Log;
import org.neo4j.neode.properties.Property;

public class DatasetManager
{
    private final GraphDatabaseService db;
    private final Log log;

    public DatasetManager( GraphDatabaseService db, Log log )
    {
        this.db = db;
        this.log = log;
    }

    public NodeSpecification nodeSpecification( String label, Property... properties )
    {
        return new NodeSpecification( label, asList( properties ), db );
    }

    public RelationshipSpecification relationshipSpecification( String label, Property... properties )
    {
        return relationshipSpecification( withName( label ), properties );
    }

    public RelationshipSpecification relationshipSpecification( RelationshipType relationshipType,
                                                                Property... properties )
    {
        return new RelationshipSpecification( relationshipType, asList( properties ), db );
    }

    public Dataset newDataset( String description )
    {
        return new Dataset( description, db, log );
    }
}