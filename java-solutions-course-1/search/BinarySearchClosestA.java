package search;

public class BinarySearchClosestA {
    //Pred: forall i: 1 to args.length-2 int(args[i])<=int(args[i+1]) &&
    // forall i: 0 to args.length-1 int(args[i]) - is defined
    //Let x = int(args[0])
    //Post: R : R=int(args[r]) : abs(x-int(args[r]))=min(x-int(args[i])) for i =1..args.length-1
    public static void main(String[] args) {
        int flag = 0;
        //flag = 0 && main:Pred
        int len = args.length +1;
        //len = args.length + 1 && flag = 0 && main:Pred
        int[] arr = new int[len];
        //len = args.length + 1 = arr.length && flag = 0 && main:Pred
        arr[0]=Integer.MIN_VALUE;
        //arr.length = len && arr[0] = min_int && flag = 0 && main:Pred
        arr[len-1]=Integer.MAX_VALUE;
        //arr.length = len && arr[0] = min_int && arr[len-1] = max_int && flag = 0 && main:Pred
        int x = Integer.parseInt(args[0]);
        //Pred: arr.length = len && arr[0] = min_int && arr[len-1] = max_int &&
        //  flag = 0 && x = int(args[0]) && main:Pred
        for (int i = 1; i < len-1; i++) {
            arr[i] = Integer.parseInt(args[i]);
            flag = (flag + arr[i])%2;
            //arr.length = len && arr[0] = min_int && arr[len-1] = max_int && flag = 0 && x = int(args[0]) && main:Pred
            //  forall j=1..i : arr[j] = int(args[j]) && main:Pred &&
            //  flag = sum(arr[j]) % 2  && j=1..i
        }
        //Post:flag = sum(arr[i]) % 2  && i=1..len-1 &&
        //  len = arr.length  && arr[0] = min_int && arr[len-1] = max_int && x = int(args[0]) &&
        //  forall i=1..len-2 : arr[i] = int(args[i]) && main:Pred

        //Let: Equivalent = (forall i=1..len-2 : arr[i] = int(args[i])) && len = arr.length = args.length+1

        int answer;
        //Pred:flag = sum(arr) % 2 && len = arr.length && arr[0] = max_int &&
        //  arr[len-1] = min_int && x = int(args[0]) && Equivalent && main:Pred
        if (flag == 1) {
            //sum(arr) % 2 == 1 && len = arr.length && arr[0] = max_int &&
            // arr[len-1] = min_int && x = int(args[0]) && Equivalent && main:Pred
            // -->
            //sum(arr) % 2 == 1 && len = arr.length && forall i:0 to len-2 arr[i]<=arr[i+1] &&
            //x = int(args[0]) && Equivalent
            answer = binarySearchIterative(arr, x, len);
            //answer = (arr[R] : abs(arr[R]-x)=min(arr[right]-x,x-arr[right-1]) && arr[right]>=x && arr[right-1]<=x &&
            // R>=1 && R<=len-2 ) || (arr[R] : arr[R]>=1 && R==1) || (arr[R-1] : arr[R-1]<=x && R==len-1) &&
            //forall i:0 to len-2 arr[i]<=arr[i+1] && x = int(args[0]) && Equivalent
            //-->
            //answer = arr[r] : abs(arr[r]-x) = min(abs(x-arr[i])) for i in range(1,len-2))
            // x = int(args[0]) && Equivalent
        } else {
            //sum(arr) % 2 == 0 && len = arr.length && arr[0] = max_int &&
            // arr[len-1] = min_int && x = int(args[0]) && Equivalent && main:Pred
            // -->
            //sum(arr) % 2 == 0 && len = arr.length && forall i:0 to len-2 arr[i]<=arr[i+1] &&
            //x = int(args[0]) && Equivalent
            answer = binarySearchRecursive(arr, x, 0, len - 1);
            //answer = (arr[R] : abs(arr[R]-x)=min(arr[right]-x,x-arr[right-1]) && arr[right]>=x && arr[right-1]<=x &&
            // R>=1 && R<=len-2 ) || (arr[R] : arr[R]>=1 && R==1) || (arr[R-1] : arr[R-1]<=x && R==len-1) &&
            //forall i:0 to len-2 arr[i]<=arr[i+1] && x = int(args[0]) && Equivalent
            //-->
            //answer = arr[r] : abs(arr[r]-x) = min(abs(x-arr[i])) for i in range(1,len-2)) &&
            // x = int(args[0]) && Equivalent
        }
        //answer = arr[r] : abs(arr[r]-x) = min(abs(x-arr[i])) for i in range(1,len-2)) &&
        // Equivalent && x = int(args[0])
        //-->
        //answer = int(args[r]) : abs(int(args[r])-x) = min(abs(x-int(args[i]))) for i in range(1,args.length-1))
        //&& x = int(args[0])
        System.out.println(answer);
        //R : R=int(args[r]) : abs(int(args[0]-int(args[r]))=min(int(args[0]-int(args[i])) for i =1..args.length-1
    }

    //Pred: forall i:0 to len-2 arr[i]<=arr[i+1] && x - int
    //Post:(R = arr[r] : abs(arr[r]-x)=min(arr[right]-x,x-arr[right-1]) && arr[right]>=x && arr[right-1]<=x &&
    // r>=1 && r<=len-2 ) || (R = arr[r] : arr[r]>=1 && r==1) || (R = arr[r-1] : arr[r-1]<=x && r==len-1) && Pred
    //Inv: Pred is always correct
    public static int binarySearchIterative(int[] arr, int x, int len) {
        int left = 0;
        int right = len-1;
        //Inv:arr[right]>=x && arr[left]<=x && right>left && left>=0 && right<=len-1
        while (right - left > 1) {
            //arr[right]>=x && arr[left]<=x && left>=0 && right<=len && right > left + 1 && left>=0 && right<=len-1
            int mid = (left + right) / 2;
            // mid > left && mid < right -->
            //Pred: arr[right]>=x && arr[left]<=x && right>mid>left && left>=0 && right<=len-1
            if (x <= arr[mid]) {
                //arr[right]>=x && arr[left]<=x && right>mid>left && x <= arr[mid] && left>=0 && mid<len-1
                //arr[mid]>=x && arr[left]<=x && right>mid>left && left>=0 && mid<len-1
                right = mid;
                //arr[right]>=x && arr[left]<=x && right>left && left>=0 && right<len-1
            } else {
                //arr[right]>=x && arr[left]<=x && right>mid>left && x > arr[mid] && left>=0 && right<=len-1
                //arr[right]>=x && arr[mid]<x && right>mid>left && mid>0 && right<=len-1
                left = mid;
                //arr[right]>=x && arr[left]<x && right>left && left>0 && right<=len-1
            }
            //Post:arr[right]>=x && arr[left]<=x && right>left && left>=0 && right<=len-1
        }
        //Post:arr[right]>=x && arr[left]<=x && right>left && right - left <=1 && left>=0 && right<=len-1
        //PostSimplified1:arr[right]>=x && arr[left]<=x && right - left ==1 && left>=0 && right<=len-1
        //PostSimplified2:arr[right]>=x && arr[right-1]<=x && right>=1 && right<=len-1
        return findClosest(arr,right,x);
    }


    //NotaBene: len = arr.length && len>=2 && x - int
    //Pred: forall i:0 to len-2 arr[i]<=arr[i+1] && left >= 0 &&
    //right <= len - 1 && right > left && arr[right]>=x && arr[left]<=x
    //Post:(R = arr[r] : abs(arr[r]-x)=min(arr[right]-x,x-arr[right-1]) && arr[right]>=x && arr[right-1]<=x &&
    // r>=1 && r<=len-2 ) || (R = arr[r] : arr[r]>=x && r==1) || (R = arr[r-1] : arr[r-1]<=x && r==len-1) && Pred
    //Inv: Pred is always correct
    public static int binarySearchRecursive(int[] arr, int x, int left, int right) {
        //arr[right]>=x && arr[left]<=x && right>left && left>=0 && right<=len-1
        if (right - left <= 1) {
            //arr[right]>=x && arr[left]<=x && right>left && right - left <=1 && left>=0 && right<=len-1
            //arr[right]>=x && arr[left]<=x && right - left ==1 && left>=0 && right<=len-1
            //arr[right]>=x && arr[right-1]>=x && right>=1 && right<=len-1
            return findClosest(arr,right,x);
        }
        //arr[right]>=x && arr[left]<=x && left>=0 && right<=len && right > left + 1 && left>=0 && right<=len-1
        int mid = (left + right) / 2;
        // mid > left && mid < right -->
        //Pred: arr[right]>=x && arr[left]<=x && right>mid>left && left>=0 && right<=len-1
        if (x <= arr[mid]) {
            //arr[right]>=x && arr[left]<=x && right>mid>left && x <= arr[mid] && left>=0 && mid<len-1
            //arr[mid]>=x && arr[left]<=x && mid>left && left>=0 && mid<len-1
            right = mid;
            //arr[right]>=x && arr[left]<=x && right>left && left>=0 && right<len-1
        } else {
            //arr[right]>=x && arr[left]<=x && right>mid>left && x > arr[mid] && left>=0 && right<=len-1
            //arr[right]>=x && arr[mid]<x && right>mid && mid>0 && right<=len-1
            left = mid;
            //arr[right]>=x && arr[left]<x && right>left && left>0 && right<=len-1
        }
        //Post: arr[right]>=x && arr[left]<=x && right>left && left>=0 && right<=len-1

        return binarySearchRecursive(arr, x, left, right);
    }
    //Pred:arr[right]>=x && arr[right-1]<=x && right>=1 && right<=len-1 && arr = [min_int, args{1:args.len-1}, max.int]
    //Post:(R = arr[r] : abs(arr[r]-x)=min(arr[right]-x,x-arr[right-1]) && arr[right]>=x && arr[right-1]<=x &&
    // r>=1 && r<=len-2 ) || (R = arr[r] : arr[r]>=x && r==1) || (R = arr[r-1] : arr[r-1]<=x && r==len-1)
    public static int findClosest(int[] arr, int right, int x){
        //Pred:arr[right]>=x && arr[right-1]<=x && right>=1 && right<=len-1
        if (right == 1){
            //arr[right]>=x && arr[right-1]=min_int<=x && right == 1
            return arr[right];
            //R = arr[right]>=x && right == 1
        }else if(right == arr.length-1){
            //arr[right]=max_int>=x && arr[right-1]<=x && right==len-1
            return arr[right-1];
            //R = arr[right-1]<=x &&  right == len-1
        }else{
            //arr[right]>=x && arr[right-1]<=x && right>=2 && right<=len-2
            if (arr[right]-x >= x-arr[right-1]){
                //arr[right]>=x && arr[right-1]<=x && right>=2 && right<=len-2 && arr[right]-x >= x-arr[right-1]
                return arr[right-1];
                //R = arr[r] : arr[r+1]>=x && arr[r] <= x && r>=1 && r<=len-3 && arr[r+1]-x >= x - arr[r]
                //R = arr[r-1] : arr[r]>=x && arr[r-1] <= x && r>=2 && r<=len-2 && arr[r]-x >= x - arr[r-1]
            }else{
                //arr[right]>=x && arr[right-1]<=x && right>=2 && right<=len-2 && arr[right]-x < x-arr[right-1]
                return arr[right];
                //R = arr[r] : arr[r]>=x && arr[r-1]<=x && r>=2 && r<=len-2 && arr[r]-x < x-arr[r-1]
            }
            //R = arr[r] : abs(arr[r]-x) = min(arr[right]-x,x-arr[right-1]) && r>=1 && r<=len-2 &&
            // arr[right]>=x && arr[right-1]<=x
        }
        //Post:R = ((arr[r] : abs(arr[r]-x) = min(arr[right]-x,x-arr[right-1]) && r>=1 && r<=len-2 &&
        // arr[right]>=x && arr[right-1]<=x) || (arr[right]>=x && right == 1) || (arr[right-1]<=x &&  right == len-1))
    }
}
