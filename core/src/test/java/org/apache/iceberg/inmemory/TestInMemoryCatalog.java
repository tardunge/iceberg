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
package org.apache.iceberg.inmemory;

import org.apache.iceberg.catalog.CatalogTests;
import org.apache.iceberg.catalog.TableIdentifier;
import org.apache.iceberg.exceptions.NoSuchNamespaceException;
import org.apache.iceberg.relocated.com.google.common.collect.ImmutableMap;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestInMemoryCatalog extends CatalogTests<InMemoryCatalog> {
  private InMemoryCatalog catalog;

  @BeforeEach
  public void before() {
    this.catalog = new InMemoryCatalog();
    this.catalog.initialize("in-memory-catalog", ImmutableMap.of());
  }

  @Override
  protected InMemoryCatalog catalog() {
    return catalog;
  }

  @Override
  protected boolean requiresNamespaceCreate() {
    return true;
  }

  @Test
  public void tableCreationWithoutNamespace() {
    Assumptions.assumeTrue(requiresNamespaceCreate());

    // this should be moved to CatalogTests at some point, but TestNessieCatalog currently fails
    // with a different exception than we would expect
    Assertions.assertThatThrownBy(
            () -> catalog().buildTable(TableIdentifier.of("ns", "table"), SCHEMA).create())
        .isInstanceOf(NoSuchNamespaceException.class)
        .hasMessage("Cannot create table ns.table. Namespace ns does not exist");
  }
}
