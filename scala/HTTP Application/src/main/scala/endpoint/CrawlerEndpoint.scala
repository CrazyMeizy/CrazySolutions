package endpoint

import domain.{CrawlerRequest, CrawlerResponse}
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.{PublicEndpoint, endpoint}

class CrawlerEndpoint {

  val crawler: PublicEndpoint[CrawlerRequest, Unit, CrawlerResponse, Any] =
    endpoint
      .tag("Crawler")
      .in("crawler")
      .post
      .summary("Returns all titles of the requested pages")
      .in(jsonBody[CrawlerRequest].example(CrawlerRequest.example))
      .out(jsonBody[CrawlerResponse].example(CrawlerResponse.example))

}
