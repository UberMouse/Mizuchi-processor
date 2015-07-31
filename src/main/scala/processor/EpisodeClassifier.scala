package processor

case class ClassifierEpisodeData(name: String, size: Long)

class EpisodeClassifier(episodeData: List[ClassifierEpisodeData]) {
  val strategies = List[EpisodeClassifierStrategy](
    new SimpleClassifier
  )

  def process: Map[Int, String] = {
    val hundredMB = 1024 * 1024 * 100
    val validFiles = episodeData.filter(_.size > hundredMB).map(_.name)
    strategies
      .map(_.process(validFiles))
      .filter{case(score, _) => score > 0}
      .sortWith{
        case((lScore, _), (rScore, _)) =>
          lScore < rScore
      }
      .head._2
      .zipWithIndex
      .map{case(l, r) => (r+1) -> l}
      .sortWith{case((_, lIndex), (_, rIndex)) => lIndex < rIndex}
      .toMap
  }
}

trait EpisodeClassifierStrategy {
  def process(fileNames: List[String]): (Int, List[String])
}

class SimpleClassifier extends EpisodeClassifierStrategy {
  val NUMBER_REGEX = "([0-9]{1,3})".r

  override def process(fileNames: List[String]): (Int, List[String]) = {
   val (success, sortedFileNames) = fileNames.sortWith(_ < _).foldLeft((List(0), List[String]())){
     case((previousNumbers, previousFileNames), fileName) =>
       val potentialEpisodeNumbers = NUMBER_REGEX.findAllIn(fileName).map(_.toInt)
       val increasingNumbers = potentialEpisodeNumbers.filter(n => previousNumbers.contains(n - 1))

       if(increasingNumbers.nonEmpty)
         (increasingNumbers.toList, previousFileNames :+ fileName)
       else
         (List(-1), previousFileNames)
     }
    if(success.contains(-1))
      (0, List[String]())
    else
      (1, sortedFileNames)
  }
}
