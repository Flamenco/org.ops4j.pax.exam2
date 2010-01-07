/*
 * Copyright (c) 2009 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.ops4j.pax.exam.raw;

import org.junit.Test;
import org.ops4j.pax.exam.Info;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.nat.internal.NativeTestContainer;
import org.ops4j.pax.exam.options.DefaultCompositeOption;
import org.ops4j.pax.exam.raw.internal.TestProbeBuilderImpl;
import org.ops4j.pax.exam.runtime.PaxExamRuntime;
import org.ops4j.pax.exam.spi.container.TestContainer;

import static org.ops4j.pax.exam.Constants.*;
import static org.ops4j.pax.exam.CoreOptions.*;

/**
 * @author Toni Menzel
 * @since Dec 2, 2009
 */
public class Sample
{

    @Test
    public void nativeDemo()
    {

        TestProbe probe = new TestProbeBuilderImpl()
            .addTest( MyCode.class, "runMe" )
            .addTest( MyCode.class, "runMeToo" )
            .get();

        // capabilities you set here are container specific.
        TestContainer container = new NativeTestContainer(
            options( felix(), getOptions() )
        );

        container.start();

        // install the probe(s)
        container.installBundle( probe.getProbe() );

        for( TestHandle handle : container.getService( TestProbe.class ).getTestHandles() )
        {
            handle.call();
        }

        container.stop();

    }

    public Option getOptions()
    {

        // always add the junit extender
        final DefaultCompositeOption option = new DefaultCompositeOption(
            mavenBundle()
                .groupId( "org.ops4j.pax.exam" )
                .artifactId( "pax-exam" )
                .version( Info.getPaxExamVersion() )
                .update( Info.isPaxExamSnapshotVersion() )
                .startLevel( START_LEVEL_SYSTEM_BUNDLES ),
            mavenBundle()
                .groupId( "org.ops4j.pax.exam" )
                .artifactId( "pax-exam-raw-extender" )
                .version( Info.getPaxExamVersion() )
                .update( Info.isPaxExamSnapshotVersion() )
                .startLevel( START_LEVEL_SYSTEM_BUNDLES ),
            mavenBundle()
                .groupId( "org.ops4j.pax.exam" )
                .artifactId( "pax-exam-raw-extender-impl" )
                .version( Info.getPaxExamVersion() )
                .update( Info.isPaxExamSnapshotVersion() )
                .startLevel( START_LEVEL_SYSTEM_BUNDLES )
        );
        return option;
    }
}