package org.neo4j.neode.probabilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.neo4j.neode.probabilities.FlatProbabilityDistributionUnique.flatDistribution;
import static org.neo4j.neode.Range.exactly;
import static org.neo4j.neode.Range.minMax;

import java.util.List;

import org.junit.Test;

public class FlatProbabilityDistributionUniqueTest
{
    @Test
    public void shouldReturnListOfNumbers() throws Exception
    {
        // given
        ProbabilityDistribution generator = flatDistribution();

        // when
        List<Integer> results = generator.generateList( 5, minMax( 1, 5 ) );

        // then
        assertTrue( results.contains( 1 ) );
        assertTrue( results.contains( 2 ) );
        assertTrue( results.contains( 3 ) );
        assertTrue( results.contains( 4 ) );
        assertTrue( results.contains( 5 ) );
    }

    @Test
    public void shouldReturnSingleNumber() throws Exception
    {
        // given
        ProbabilityDistribution generator = flatDistribution();

        // when
        int result = generator.generateSingle( exactly( 1 ) );

        // then
        assertEquals( 1, result );
    }

    @Test
    public void shouldReturnTwoResults() throws Exception
    {
        // given
        ProbabilityDistribution generator = flatDistribution();

        // when
        List<Integer> results = generator.generateList( 2, minMax( 0, 1 ) );

        // then
        assertTrue( results.contains( 0 ) );
        assertTrue( results.contains( 1 ) );
    }
}
