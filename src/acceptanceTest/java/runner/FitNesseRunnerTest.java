package runner;

import fitnesse.junit.FitNesseRunner;
import org.junit.runner.RunWith;

@RunWith(FitNesseRunner.class)
@FitNesseRunner.Suite("_root")
@FitNesseRunner.FitnesseDir(value = "src/acceptanceTest/specification", fitNesseRoot = ".")
@FitNesseRunner.OutputDir("./build/fitnesse-results")
public class FitNesseRunnerTest {

}
