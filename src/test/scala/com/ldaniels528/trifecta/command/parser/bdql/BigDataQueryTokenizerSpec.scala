package com.ldaniels528.trifecta.command.parser.bdql

import org.scalatest.Matchers._
import org.scalatest.{FeatureSpec, GivenWhenThen}

/**
 * Big Data Query Language (BD-QL) Tokenizer Specification
 * @author Lawrence Daniels <lawrence.daniels@gmail.com>
 */
class BigDataQueryTokenizerSpec() extends FeatureSpec with GivenWhenThen {

  info("As a Big Data Query Tokenizer")
  info("I want to be able to parse Big Data queries into query objects")

  feature("Ability to parse Big Data queries into BD-QL objects") {
    scenario("A string containing a Big Data selection query") {
      Given("a Big Data query string")
      val queryString =
        """
          |select symbol, exchange, lastTrade, volume
          |from "topic:shocktrade.quotes.avro" with "avro:file:~/avro/quotes.avsc"
          |into elastic_search_quotes
          |where exchange = 'OTCBB'
          |and lastTrade <= 1.0
          |and volume >= 1,000,000
          |limit 10
          | """.stripMargin

      When("The query is parsed into tokens")
      val tokens = BigDataQueryTokenizer.parse(queryString)

      Then("The arguments should be successfully verified")
      info(s"results: ${tokens map (s => s""""$s"""") mkString " "}")
      tokens shouldBe Seq(
        "select", "symbol", ",", "exchange", ",", "lastTrade", ",", "volume", "from",
        "\"topic:shocktrade.quotes.avro\"", "with", "\"avro:file:~/avro/quotes.avsc\"",
        "into", "elastic_search_quotes", "where", "exchange", "=", "'OTCBB'", "and", "lastTrade", "<=",
        "1.0", "and", "volume", ">=", "1,000,000", "limit", "10")
    }
  }

}
