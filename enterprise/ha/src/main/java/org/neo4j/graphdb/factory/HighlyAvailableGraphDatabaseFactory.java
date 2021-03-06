/**
 * Copyright (c) 2002-2015 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.graphdb.factory;

import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.ha.HighlyAvailableGraphDatabase;

/**
 * Factory for HA Neo4j instances.
 */
public class HighlyAvailableGraphDatabaseFactory extends GraphDatabaseFactory
{
    @Override
    protected GraphDatabaseBuilder.DatabaseCreator createDatabaseCreator(
            final String path, final GraphDatabaseFactoryState state )
    {
        return new GraphDatabaseBuilder.DatabaseCreator()
        {

            @Override
            public GraphDatabaseService newDatabase( final Map<String, String> config )
            {
                config.put( "ephemeral", "false" );

                return new HighlyAvailableGraphDatabase( path, config, state.databaseDependencies() );
            }
        };
    }

    /**
     * @deprecated By using
     *             {@link HighlyAvailableGraphDatabaseFactory#newEmbeddedDatabase(String)}
     *             you get an abstraction of this factory, so you can either use
     *             this factory or {@link GraphDatabaseFactory}.
     */
    @Deprecated
    public GraphDatabaseService newHighlyAvailableDatabase( final String path )
    {
        return newEmbeddedDatabase( path );
    }

    /**
     * @deprecated By using
     *             {@link HighlyAvailableGraphDatabaseFactory#newEmbeddedDatabaseBuilder(String)}
     *             you get an abstraction of this factory, so you can either use
     *             this factory or {@link GraphDatabaseFactory}.
     */
    @Deprecated
    public GraphDatabaseBuilder newHighlyAvailableDatabaseBuilder( final String path )
    {
        return newEmbeddedDatabaseBuilder( path );
    }
}
