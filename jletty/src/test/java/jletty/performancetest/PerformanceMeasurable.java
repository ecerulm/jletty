package jletty.performancetest;

import java.util.Map;

public interface PerformanceMeasurable {
  Map getPerformanceMeasures(int numCycles);
  String getName();
}
