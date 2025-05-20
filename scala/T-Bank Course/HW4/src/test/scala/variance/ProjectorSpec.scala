package variance

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ProjectorSpec extends AnyFlatSpec with Matchers {
  "Projector[RedactedWordLine]" should
    "project some words hidden from Slide[RedactedWordLine] with Converter[RedactedWordLine]" in {
      val projector = new Projector[RedactedWordLine](RedactedLineConverter)
      val screen = new HelloSlide[RedactedWordLine](
        Seq(new RedactedWordLine(0.0, "aba"), new RedactedWordLine(1.0, "caba"))
      )
      projector.project(screen) should be("███\ncaba\n")
    }

  "Projector[RedactedWordLine]" should "project text from Slide[RedactedWordLine]" in {
    val projector = new Projector[RedactedWordLine](LineConverter)

    val screenRWL = new HelloSlide[RedactedWordLine](
      Seq(new RedactedWordLine(0.0, "aba"), new RedactedWordLine(1.0, "caba"))
    )

    projector.project(screenRWL) should be("aba\ncaba\n")
  }

  "Projector[RedactedWordLine]" should "be able to use Converter[RedactedWordLine] and Converter[WordLine]" in {
    val projectorRLC = new Projector[RedactedWordLine](RedactedLineConverter)
    val projectorLC = new Projector[RedactedWordLine](LineConverter)

    val screenRWL = new HelloSlide[RedactedWordLine](
      Seq(new RedactedWordLine(0.0, "aba"), new RedactedWordLine(1.0, "caba"))
    )

    projectorRLC.project(screenRWL) should be("███\ncaba\n")
    projectorLC.project(screenRWL) should be("aba\ncaba\n")
  }

  "Projector[RedactedWordLine]" should "not project text from Slide[WordLine] " in {
    val projector = new Projector[RedactedWordLine](LineConverter)

    val screenWL = new HelloSlide[WordLine](
      Seq(new WordLine("aba"), new RedactedWordLine(0.8, "caba"))
    )

    "projector.project(screenRWL)" shouldNot typeCheck
  }

  "Projector[WordLine]" should "be able to use Converter[WordLine]" in {
    val projectorLC = new Projector[WordLine](LineConverter)

    val screenRWL = new HelloSlide[RedactedWordLine](
      Seq(new RedactedWordLine(0.0, "aba"), new RedactedWordLine(1.0, "caba"))
    )

    projectorLC.project(screenRWL) should be("aba\ncaba\n")
  }

  "Projector[WordLine]" should "not be able to use Converter[RedactedWordLine]" in {
    "new Projector[WordLine](RedactedLineConverter)" shouldNot typeCheck
  }

  "Projector[WordLine]" should "project text from Slide[WordLine] and Slide[RedactedWordLine]" in {
    val projector = new Projector[WordLine](LineConverter)

    val screenWL = new HelloSlide[WordLine](
      Seq(new WordLine("aba"), new RedactedWordLine(0.8, "caba"))
    )
    val screenRWL = new HelloSlide[RedactedWordLine](
      Seq(new RedactedWordLine(0.0, "aba"), new RedactedWordLine(1.0, "caba"))
    )

    projector.project(screenRWL) should be("aba\ncaba\n")
    projector.project(screenWL) should be("aba\ncaba\n")
  }
}
