/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.scm.plugin;

import java.io.File;

import org.apache.maven.scm.provider.svn.SvnScmTestUtils;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.apache.maven.scm.ScmTestCase.checkScmPresence;

/**
 * @author <a href="mailto:evenisse@apache.org">Emmanuel Venisse</a>
 *
 */
@RunWith(JUnit4.class)
public class UpdateMojoTest extends AbstractJUnit4MojoTestCase {
    File checkoutDir;

    File repository;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        checkoutDir = getTestFile("target/checkout");

        repository = getTestFile("target/repository");

        FileUtils.forceDelete(checkoutDir);
    }

    @Test
    public void testSkipCheckoutWithConnectionUrl() throws Exception {
        checkScmPresence(SvnScmTestUtils.SVNADMIN_COMMAND_LINE);

        SvnScmTestUtils.initializeRepository(repository);

        checkScmPresence(SvnScmTestUtils.SVN_COMMAND_LINE);

        CheckoutMojo checkoutMojo = (CheckoutMojo)
                lookupMojo("checkout", getTestFile("src/test/resources/mojos/checkout/checkoutWithConnectionUrl.xml"));

        String connectionUrl = checkoutMojo.getConnectionUrl();
        connectionUrl = StringUtils.replace(connectionUrl, "${basedir}", getBasedir());
        connectionUrl = StringUtils.replace(connectionUrl, "\\", "/");
        checkoutMojo.setConnectionUrl(connectionUrl);

        checkoutMojo.execute();

        UpdateMojo updateMojo = (UpdateMojo)
                lookupMojo("update", getTestFile("src/test/resources/mojos/update/updateWithConnectionUrl.xml"));

        connectionUrl = updateMojo.getConnectionUrl();
        connectionUrl = StringUtils.replace(connectionUrl, "${basedir}", getBasedir());
        connectionUrl = StringUtils.replace(connectionUrl, "\\", "/");
        updateMojo.setConnectionUrl(connectionUrl);

        updateMojo.execute();
    }
}
