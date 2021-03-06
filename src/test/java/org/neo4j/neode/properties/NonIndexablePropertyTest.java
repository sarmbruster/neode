package org.neo4j.neode.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Transaction;
import org.neo4j.neode.test.Db;

public class NonIndexablePropertyTest
{
    @Test
    public void shouldSetPropertyValue() throws Exception
    {
        // given
        PropertyValueGenerator generator = new PropertyValueGenerator()
        {
            @Override
            public Object generateValue( PropertyContainer propertyContainer, String nodeLabel, int iteration )
            {
                return "value";
            }
        };
        Property property = new NonIndexableProperty( "name", generator );

        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node node = db.createNode();

        // when
        property.setProperty( node, db, "user", 1 );
        tx.success();
        tx.finish();

        // then
        assertEquals( "value", node.getProperty( "name" ) );
        assertNull( db.index().forNodes( "user" ).get( "name", "value" ).getSingle() );
    }
}
