package org.neo4j.neode.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

import java.util.Iterator;
import java.util.Random;

import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.neode.test.Db;

public class RelationshipUniquenessTest
{
    @Test
    public void uniqueSingleDirectionShouldNotCreateNewRelationshipIfRelationshipAlreadyExists() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        firstNode.createRelationshipTo( secondNode, friend_of );

        RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.SINGLE_DIRECTION;

        // when
        relationshipUniqueness.createRelationship( db, firstNode, secondNode,
                new RelationshipInfo( friend_of, Direction.OUTGOING ), new Random() );
        tx.success();
        tx.finish();

        // then
        Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.OUTGOING ).iterator();
        assertNotNull( iterator.next() );
        assertFalse( iterator.hasNext() );
    }

    @Test
    public void uniqueSingleDirectionShouldCreateNewRelationshipIfRelationshipAlreadyExistsInOppositeDirection()
            throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        secondNode.createRelationshipTo( firstNode, friend_of );

        RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.SINGLE_DIRECTION;

        // when
        relationshipUniqueness.createRelationship( db, firstNode, secondNode,
                new RelationshipInfo( friend_of, Direction.OUTGOING ), new Random() );
        tx.success();
        tx.finish();

        // then
        Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.BOTH ).iterator();
        assertNotNull( iterator.next() );
        assertNotNull( iterator.next() );
        assertFalse( iterator.hasNext() );
    }

    @Test
    public void uniqueBothDirectionsShouldNotCreateNewRelationshipIfRelationshipAlreadyExists() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        firstNode.createRelationshipTo( secondNode, friend_of );

        RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.BOTH_DIRECTIONS;

        // when
        relationshipUniqueness.createRelationship( db, firstNode, secondNode,
                new RelationshipInfo( friend_of, Direction.OUTGOING ), new Random() );
        tx.success();
        tx.finish();

        // then
        Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.OUTGOING ).iterator();
        assertNotNull( iterator.next() );
        assertFalse( iterator.hasNext() );
    }

    @Test
    public void uniqueBothDirectionsShouldNotCreateNewRelationshipIfRelationshipAlreadyExistsInOppositeDirection()
            throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        secondNode.createRelationshipTo( firstNode, friend_of );

        RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.BOTH_DIRECTIONS;

        // when
        relationshipUniqueness.createRelationship( db, firstNode, secondNode,
                new RelationshipInfo( friend_of, Direction.OUTGOING ), new Random() );
        tx.success();
        tx.finish();

        // then
        Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.BOTH ).iterator();
        assertNotNull( iterator.next() );
        assertFalse( iterator.hasNext() );
    }

    @Test
    public void allowMultipleShouldCreateNewRelationshipEvenIfRelationshipAlreadyExists() throws Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        firstNode.createRelationshipTo( secondNode, friend_of );

        RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.ALLOW_MULTIPLE;

        // when
        relationshipUniqueness.createRelationship( db, firstNode, secondNode,
                new RelationshipInfo( friend_of, Direction.OUTGOING ), new Random() );
        tx.success();
        tx.finish();

        // then
        Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.OUTGOING ).iterator();
        assertNotNull( iterator.next() );
        assertNotNull( iterator.next() );
        assertFalse( iterator.hasNext() );
    }

    @Test
    public void allowMultipleShouldCreateNewRelationshipEvenIfRelationshipAlreadyExistsInOppositeDirection() throws
            Exception
    {
        // given
        GraphDatabaseService db = Db.impermanentDb();
        Transaction tx = db.beginTx();
        Node firstNode = db.createNode();
        Node secondNode = db.createNode();
        DynamicRelationshipType friend_of = withName( "FRIEND_OF" );
        secondNode.createRelationshipTo( firstNode, friend_of );

        RelationshipUniqueness relationshipUniqueness = RelationshipUniqueness.ALLOW_MULTIPLE;

        // when
        relationshipUniqueness.createRelationship( db, firstNode, secondNode,
                new RelationshipInfo( friend_of, Direction.OUTGOING ), new Random() );
        tx.success();
        tx.finish();

        // then
        Iterator<Relationship> iterator = firstNode.getRelationships( friend_of, Direction.BOTH ).iterator();
        assertNotNull( iterator.next() );
        assertNotNull( iterator.next() );
        assertFalse( iterator.hasNext() );
    }
}