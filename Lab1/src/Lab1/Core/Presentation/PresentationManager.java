package Lab1.Core.Presentation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.style.Styler.LegendPosition;

import Lab1.Core.Question;
import Lab1.Core.Test;
import Lab1.Core.Student.StudentCard;
import Lab1.Core.Student.StudentQuestionAnswer;

public class PresentationManager {
	PresentationResult testResult = null;

	public PresentationResult calculateForTest(Test test) {
		if(test.getStudents().size() == 0)
			return null;
		
		PresentationResult r = new PresentationResult(test);
		
		//Question statistics
		for(Question q : test.getQuestions()) {
			r.getQuestionStatistics().add(evaluateQuestion(q.getId(), test));
		}
		
		return testResult = r;
	}
	
	public boolean canGenerateHistogram() {
		
		return getTestResult() != null && getTestResult().getTest() != null;
	}

	public PresentationResult getTestResult() {
		return testResult;
	}

	private QuestionStatistic evaluateQuestion(int questionId, Test test) {
		
		int correct = 0;
		int incorrect = 0;
		
		for(StudentCard sa: test.getStudents()) {
			
			List<StudentQuestionAnswer> saForQuestions = sa.getAnswersForQuestion(questionId);
			for(StudentQuestionAnswer a: saForQuestions) {
				
				boolean isCorrect = test.isQuestionAnswerCorrect(a);
				if(isCorrect)
					correct++;
				else
					incorrect++;
			}
		}
		
		QuestionStatistic stat = new QuestionStatistic();
		stat.setQuestionId(questionId);
		stat.setStudentsAnsweredCorrectly(correct);
		stat.setStudentsAnsweredIncorrectly(incorrect);
		
		return stat;
	}

}
