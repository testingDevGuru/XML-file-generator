package xml.file.generator;

public class SumHelper {
	
	
	public int [] sumTargetCalc(int [] arr, int target) {
		boolean sumFound = false;
		
		int []  resultArr = new int [2];
		for (int i = 0; i< arr.length;i++) {
			for(int y = i+1; y<arr.length;y++) {
				if((arr[i]+arr[y])==target) {
					resultArr[0] = i;
					resultArr[1]=y;
					sumFound = true;
					break;
					
				}
			}
			if (sumFound == true) {
				break;
			}
		}
		
		
		return resultArr;
		
	}
	
	public static void main(String[] args) {
		SumHelper sumHelper = new SumHelper();
		
		int[] givenArray = {1,5,3,4,2};
		int target = 7;
		
		int [] outputArray = sumHelper.sumTargetCalc(givenArray, target);
		
		for (int i = 0;i<outputArray.length;i++) {
			System.out.println(outputArray[i]);
		}
		
		
		
	}

}
