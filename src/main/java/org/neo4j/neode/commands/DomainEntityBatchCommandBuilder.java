package org.neo4j.neode.commands;

import org.neo4j.neode.Dataset;
import org.neo4j.neode.DomainEntity;
import org.neo4j.neode.DomainEntityInfo;
import org.neo4j.neode.commands.interfaces.Update;

public class DomainEntityBatchCommandBuilder implements Update
{
    private static final int DEFAULT_BATCH_SIZE = 20000;

    private DomainEntity domainEntity;
    private int numberOfIterations = 0;

    public DomainEntityBatchCommandBuilder( DomainEntity domainEntity )
    {
        this.domainEntity = domainEntity;
    }

    public DomainEntityBatchCommandBuilder quantity( int value )
    {
        numberOfIterations = value;
        return this;
    }

    @Override
    public DomainEntityInfo update( Dataset dataset, int batchSize )
    {
        DomainEntityBatchCommand command =
                new DomainEntityBatchCommand( domainEntity, numberOfIterations, batchSize, new EndNodeIdCollector() );
        return dataset.execute( command );
    }

    @Override
    public DomainEntityInfo update( Dataset dataset )
    {
        return update( dataset, DEFAULT_BATCH_SIZE );
    }

    @Override
    public void updateNoReturn( Dataset dataset, int batchSize )
    {
        DomainEntityBatchCommand command =
                new DomainEntityBatchCommand( domainEntity, numberOfIterations, batchSize,
                        NullEndNodeIdCollector.INSTANCE );
        dataset.execute( command );
    }

    @Override
    public void updateNoReturn( Dataset dataset )
    {
        updateNoReturn( dataset, DEFAULT_BATCH_SIZE );
    }

}
