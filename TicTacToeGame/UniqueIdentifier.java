package javaapplication12;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Source: https://www.youtube.com/playlist?list=PLlrATfBNZ98cCvNtS7x4u1bZOS5FBwOSb

//class that stores a list of unique ids that are given to clients when they connect to the server
public class UniqueIdentifier {

	private static List<Integer> ids = new ArrayList<Integer>();
	private static final int RANGE = 10000;

	private static int index = 0;

	static {
		for (int i = 0; i < RANGE; i++) {
			ids.add(i);
		}
		Collections.shuffle(ids);
	}

	private UniqueIdentifier() {
	}

	public static int getIdentifier() {
		if (index > ids.size() - 1) index = 0;
		return ids.get(index++);
	}

}
