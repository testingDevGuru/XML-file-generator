package xml.file.generator;

import java.util.ArrayList;
import java.util.List;

class WarmUp {

	public List<Integer> arrayModifier() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 50; i <= 100; i++) {
			if (i % 3 == 0 && i % 5 == 0) {
				list.add(i);
			}
		}
		return list;

	}

	public static void main(String[] args) {
		WarmUp w = new WarmUp();
		List<Integer> result = new ArrayList<Integer>();
		result = w.arrayModifier();
		for (Integer num : result) {
			System.out.println("The Number that can be divided by 3 and 5 without remainder: "+num);
				
		}

	}
}