/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.soletraderidentification.services

import org.mockito.scalatest.{IdiomaticMockito, ResetMocksAfterEachTest}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.{JsString, Json}
import play.api.test.Helpers._
import uk.gov.hmrc.soletraderidentification.repositories.JourneyDataRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class JourneyDataServiceSpec extends AnyWordSpec with Matchers with IdiomaticMockito with ResetMocksAfterEachTest {

  val mockJourneyDataRepository: JourneyDataRepository = mock[JourneyDataRepository]

  object TestJourneyDataService extends JourneyDataService(mockJourneyDataRepository)

  val testJourneyId = "testJourneyId"
  val testInternalId = "testInternalId"

  "getJourneyData" should {
    "return the stored journey data" when {
      "the data exists in the database" in {
        val testJourneyData = Json.obj("testKey" -> "testValue")

        mockJourneyDataRepository.getJourneyData(testJourneyId, testInternalId) returns Future.successful(Some(testJourneyData))

        await(TestJourneyDataService.getJourneyData(testJourneyId, testInternalId)) mustBe Some(testJourneyData)
      }
    }
    "return None" when {
      "the data does not exist in the database" in {
        mockJourneyDataRepository.getJourneyData(testJourneyId, testInternalId) returns Future.successful(None)

        await(TestJourneyDataService.getJourneyData(testJourneyId, testInternalId)) mustBe None
      }
    }
  }

  "getJourneyDataByKey" should {
    "return the stored journey data for the key provided" when {
      "the data exists in the database" in {
        val testKey = "testKey"
        val testValue = "testValue"

        val testJourneyData = Json.obj(testKey -> testValue)

        mockJourneyDataRepository.getJourneyData(testJourneyId, testInternalId) returns Future.successful(Some(testJourneyData))

        await(TestJourneyDataService.getJourneyDataByKey(testJourneyId, testKey, testInternalId)) mustBe Some(JsString(testValue))

      }
    }
    "return None" when {
      "the data does not exist in the database" in {
        val testKey = "testKey"

        mockJourneyDataRepository.getJourneyData(testJourneyId, testInternalId) returns Future.successful(None)

        await(TestJourneyDataService.getJourneyDataByKey(testJourneyId, testKey, testInternalId)) mustBe None
      }
    }
  }

}