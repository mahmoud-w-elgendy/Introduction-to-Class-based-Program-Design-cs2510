package assignment2.problem1;

import tester.Tester;

class EmbroideryPiece {
  String name;
  IMotif motif;

  public EmbroideryPiece(String name, IMotif motif) {
    this.name = name;
    this.motif = motif;
  }

  // computes the average difficulty of all the cross-stitch and chain-stitch motifs
  // in this.motif
  double averageDifficulty() {
    return motif.averageDifficulty();
  }

  String embroideryInfo() {
    return name + ": " + motif.info() + ".";
  }
}

// The Motif interface represents the different types of motifs.
interface IMotif {
  double averageDifficulty();

  AvgInfo averageDifficultyHelp(AvgInfo info);

  String info();
}

// The CrossStitchMotif class implements the Motif interface.
class CrossStitchMotif implements IMotif {
  String description;
  double difficulty;

  CrossStitchMotif(String description, double difficulty) {
    this.description = description;
    this.difficulty = difficulty;
  }

  public double averageDifficulty() {
    return difficulty;
  }

  public AvgInfo averageDifficultyHelp(AvgInfo info) {
    return info.add(this.difficulty, 1);
  }

  public String info() {
    return description + " (cross stitch)";
  }
}

// The ChainStitchMotif class implements the Motif interface.
class ChainStitchMotif implements IMotif {
  String description;
  double difficulty;

  ChainStitchMotif(String description, double difficulty) {
    this.description = description;
    this.difficulty = difficulty;
  }

  public double averageDifficulty() {
    return difficulty;
  }

  public AvgInfo averageDifficultyHelp(AvgInfo info) {
    return info.add(this.difficulty, 1);
  }

  public String info() {
    return description + " (chain stitch)";
  }
}

// The GroupMotif class implements the Motif interface and contains a list of other motifs.
class GroupMotif implements IMotif {
  String description;
  ILoMotif motifs;

  GroupMotif(String description, ILoMotif motifs) {
    this.description = description;
    this.motifs = motifs;
  }

  public double averageDifficulty() {
    return motifs.averageDifficulty();
  }

  public AvgInfo averageDifficultyHelp(AvgInfo info) {
    return motifs.averageDifficultyHelp(info);
  }

  public String info() {
    return motifs.info();
  }
}

// The ILoMotif interface represents a list of Motifs.
interface ILoMotif {
  double averageDifficulty();
  AvgInfo averageDifficultyHelp(AvgInfo info);
  String info();

}

// The MtLoMotif class represents an empty list of Motifs.
class MtLoMotif implements ILoMotif {
  public double averageDifficulty() {
    return 0;
  }

  public AvgInfo averageDifficultyHelp(AvgInfo info) {
    return info;
  }

  public String info() {
    return "";
  }
}

// The ConsLoMotif class represents a non-empty list of Motifs.
class ConsLoMotif implements ILoMotif {
  IMotif first;
  ILoMotif rest;

  ConsLoMotif(IMotif first, ILoMotif rest) {
    this.first = first;
    this.rest = rest;
  }

  public double averageDifficulty() {
    AvgInfo info = averageDifficultyHelp(new AvgInfo(0, 0));
    return info.compute();
  }

  public AvgInfo averageDifficultyHelp(AvgInfo info) {
    return rest.averageDifficultyHelp(first.averageDifficultyHelp(info));
  }

  public String info() {
    if (rest.info().isEmpty()) {
      return first.info();
    }
    return first.info() + ", " + rest.info();
  }
}


class AvgInfo {
  double sum;
  int numOfItems;

  public AvgInfo(double sum, int numOfItems) {
    this.sum = sum;
    this.numOfItems = numOfItems;
  }

  AvgInfo add(AvgInfo that) {
    return that.add(sum, numOfItems);
  }

  AvgInfo add(double sum, int numOfItems) {
    return new AvgInfo(this.sum + sum, this.numOfItems + numOfItems);
  }

  double compute() {
    return sum / numOfItems;
  }
}


class ExamplesEmbroidery {
  IMotif bird = new CrossStitchMotif("bird", 4.5);
  IMotif tree = new ChainStitchMotif("tree", 3.0);
  IMotif rose = new CrossStitchMotif("rose", 5.0);
  IMotif poppy = new ChainStitchMotif("poppy", 4.75);
  IMotif daisy = new CrossStitchMotif("daisy", 3.2);

  IMotif flowers = new GroupMotif("flowers", new ConsLoMotif(rose,
      new ConsLoMotif(poppy, new ConsLoMotif(daisy, new MtLoMotif()))));
  IMotif nature = new GroupMotif("nature", new ConsLoMotif(bird,
      new ConsLoMotif(tree, new ConsLoMotif(flowers, new MtLoMotif()))));

  EmbroideryPiece pillowCover = new EmbroideryPiece("Pillow Cover", nature);

  boolean testAverageDifficulty(Tester t) {
    return t.checkInexact(bird.averageDifficulty(), 4.5, 0.01) &&
        t.checkInexact(tree.averageDifficulty(), 3.0, 0.01) &&
        t.checkInexact(flowers.averageDifficulty(), 4.31, 0.01) &&
        t.checkInexact(pillowCover.averageDifficulty(), 4.09, 0.01);
  }

  boolean testEmbroideryInfo(Tester t) {
    return t.checkExpect(pillowCover.embroideryInfo(), "Pillow Cover: bird (cross stitch), tree "
        + "(chain stitch), rose (cross stitch), poppy (chain stitch), daisy (cross stitch).") &&
        t.checkExpect(tree.info(), "tree (chain stitch)");
  }
}