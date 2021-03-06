/**
 * Copyright (c) 2002-2015 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.internal.compiler

import org.neo4j.cypher.internal._
import org.neo4j.cypher.internal.commons.CypherFunSuite
import org.neo4j.cypher.internal.compiler.v2_3.parser.ParserMonitor

class CypherOptionParserTest extends CypherFunSuite {

  def parse(arg:String): CypherQueryWithOptions = {
    CypherOptionParser(mock[ParserMonitor[CypherQueryWithOptions]]).apply(arg)
  }

  test("should parse version") {
    parse("CYPHER 1.9 MATCH") should equal(CypherQueryWithOptions("MATCH", Seq(VersionOption("1.9"))))
    parse("CYPHER 2.0 THAT") should equal(CypherQueryWithOptions("THAT", Seq(VersionOption("2.0"))))
    parse("CYPHER 2.1 YO") should equal(CypherQueryWithOptions("YO", Seq(VersionOption("2.1"))))
    parse("CYPHER 2.2 HO") should equal(CypherQueryWithOptions("HO", Seq(VersionOption("2.2"))))
  }

  test("should parse profile") {
    parse("PROFILE THINGS") should equal(CypherQueryWithOptions("THINGS", Seq(ProfileOption)))
  }

  test("should parse explain") {
    parse("EXPLAIN THIS") should equal(CypherQueryWithOptions("THIS", Seq(ExplainOption)))
  }

  test("should parse multiple options") {
    parse("CYPHER 2.2 PLANNER COST PROFILE PATTERN") should equal(CypherQueryWithOptions("PATTERN", Seq(VersionOption("2.2"), CostPlannerOption, ProfileOption)))
    parse("EXPLAIN CYPHER 2.1 YALL") should equal(CypherQueryWithOptions("YALL", Seq(ExplainOption, VersionOption("2.1"))))
  }

  test("should require whitespace between option and query") {
    parse("explainmatch") should equal(CypherQueryWithOptions("explainmatch"))
    parse("explain match") should equal(CypherQueryWithOptions("match", Seq(ExplainOption)))
  }

  test("should parse version and planner/compiler") {
    parse("CYPHER 2.2 PLANNER COST RETURN") should equal(CypherQueryWithOptions("RETURN", Seq(VersionOption("2.2"), CostPlannerOption)))
    parse("PLANNER COST RETURN") should equal(CypherQueryWithOptions("RETURN",Seq(CostPlannerOption)))
    parse("CYPHER 2.2 PLANNER RULE RETURN") should equal(CypherQueryWithOptions("RETURN", Seq(VersionOption("2.2"), RulePlannerOption)))
    parse("PLANNER RULE RETURN") should equal(CypherQueryWithOptions("RETURN", Seq(RulePlannerOption)))
    parse("CYPHER 2.2 PLANNER IDP RETURN") should equal(CypherQueryWithOptions("RETURN", Seq(VersionOption("2.2"), IDPPlannerOption)))
    parse("CYPHER 2.2 PLANNER DP RETURN") should equal(CypherQueryWithOptions("RETURN", Seq(VersionOption("2.2"), DPPlannerOption)))
    parse("PLANNER IDP RETURN") should equal(CypherQueryWithOptions("RETURN",Seq(IDPPlannerOption)))
    parse("PLANNER DP RETURN") should equal(CypherQueryWithOptions("RETURN",Seq(DPPlannerOption)))
  }
}
