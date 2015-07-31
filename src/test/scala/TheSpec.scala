import processor.{ClassifierEpisodeData, EpisodeClassifier}

/**
 * Created by taylor on 31/07/15.
 */
class TheSpec extends UnitSpec {

  "episodeClassifier" should "order shows according to their episode numbers" in {
    val gigabyte = 1024 * 1024 * 1024

    val classifier = new EpisodeClassifier(List(
      ClassifierEpisodeData("[Commie] Nisekoi 2 - 03 [D0B9A09D].mkv", gigabyte),
      ClassifierEpisodeData("[Commie] Nisekoi 2 - 02 [363BB699].mkv", gigabyte),
      ClassifierEpisodeData("[Commie] Nisekoi 2 - 05 [68DD1A6E].mkv", gigabyte),
      ClassifierEpisodeData("[Commie] Nisekoi 2 - 01 [4EA59436].mkv", gigabyte),
      ClassifierEpisodeData("[Commie] Nisekoi 2 - 04 [381202E7].mkv", gigabyte)
    ))

    classifier.process should be (
      Map[Int, String](
        1 -> "[Commie] Nisekoi 2 - 01 [4EA59436].mkv",
        2 -> "[Commie] Nisekoi 2 - 02 [363BB699].mkv",
        3 -> "[Commie] Nisekoi 2 - 03 [D0B9A09D].mkv",
        4 -> "[Commie] Nisekoi 2 - 04 [381202E7].mkv",
        5 -> "[Commie] Nisekoi 2 - 05 [68DD1A6E].mkv"
      )
    )
  }

  "episodeClassifier" should "reject invalid files" in {
    val gigabyte = 1024 * 1024 * 1024

    val classifier = new EpisodeClassifier(List(
      ClassifierEpisodeData("[Commie] Nisekoi 2 - 03 [D0B9A09D].mkv", gigabyte),
      ClassifierEpisodeData("[Commie] Nisekoi 2 - 02 [363BB699].mkv", gigabyte),
      ClassifierEpisodeData("[Commie] Nisekoi 2 - 05 [68DD1A6E].mkv", gigabyte),
      ClassifierEpisodeData("[Commie] Nisekoi 2 - 01 [4EA59436].mkv", gigabyte),
      ClassifierEpisodeData("[Commie] Nisekoi 2 - ED1 [4EA59436].mkv",    1024),
      ClassifierEpisodeData("[Commie] Nisekoi 2 - 04 [381202E7].mkv", gigabyte)
    ))

    classifier.process should be (
      Map[Int, String](
        1 -> "[Commie] Nisekoi 2 - 01 [4EA59436].mkv",
        2 -> "[Commie] Nisekoi 2 - 02 [363BB699].mkv",
        3 -> "[Commie] Nisekoi 2 - 03 [D0B9A09D].mkv",
        4 -> "[Commie] Nisekoi 2 - 04 [381202E7].mkv",
        5 -> "[Commie] Nisekoi 2 - 05 [68DD1A6E].mkv"
      )
    )
  }
}
