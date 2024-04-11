package search;

public class BinarySearch {
    //Pred: forall i: 1 to args.length-2 int(args[i])>=int(args[i+1]) &&
    // forall i: 0 to args.length-1 int(args[i]) - is defined
    //Let x = int(args[0])
    //Post: R : 1<=R<=args.length-2 && int(args[R+1]) <= x && int(args[R]) > x ||
    //R==args.length-1 && int(args[R])>x || R == 0 && args[R+1] <= x
    public static void main(String[] args) {
        int len = args.length +1;
        //len = args.length + 1 && main:Pred
        int[] arr = new int[len];
        //len = args.length + 1 = arr.length && main:Pred
        arr[0]=Integer.MAX_VALUE;
        //arr.length = len && arr[0] = max_int && main:Pred
        arr[len-1]=Integer.MIN_VALUE;
        //arr.length = len && arr[0] = max_int && arr[len-1] = min_int && main:Pred
        int x = Integer.parseInt(args[0]);
        //arr.length = len && arr[0] = max_int && arr[len-1] = min_int && x = int(args[0]) && main:Pred
        for (int i = 1; i < len-1; i++) {
            //arr.length = len && arr[0] = max_int && arr[len-1] = min_int && x = int(args[0]) && main:Pred
            arr[i] = Integer.parseInt(args[i]);
            //arr.length = len && arr[0] = max_int && arr[len-1] = min_int && x = int(args[0]) &&
            //  forall j=1..i : arr[j] = int(args[j]) && main:Pred
        }
        //arr.length = len && arr[0] = max_int && arr[len-1] = min_int && x = int(args[0]) &&
        //  forall i=1..len-2 : arr[i] = int(args[i]) && main:Pred

        //forall i= 1..len-2 : arr[i]=int(args[i]) && arr[0]=max_int && arr[len-1] = min_int &&
        //  forall i=0..len-1 : arr[i]>=arr[i+1] && x = int(args[0]) && len = arr.length
        int answer = binarySearchIterative(arr, x, len);
        /*int answer = binarySearchRecursive(arr, x, 0,len-1);*/

        //answer = R : R>=0 && R<=len-2 && arr[R+1]<=x && arr[R]>=x && x = int(args[0]) &&
        // forall i= 1..len-2 : arr[i]=int(args[i])

        // answer = R : (1<=R<=len-3 && arr[R+1] <= x && arr[R] > x ||
        //  R == len-2 && arr[R]>x || R == 0 && arr[R+1] <= x) && x = int(args[0])

        //answer = R : 1<=R<=args.length-2 && int(args[R+1]) <= int(args[0]) && int(args[R]) > int(args[0]) ||
        //R==args.length-1 && int(args[R])>int(args[0]) || R == 0 && args[R+1] <= int(args[0])
        System.out.println(answer);
    }
    //Pred: forall i=0..len-2 arr[i]>=arr[i+1] && x = int
    //Post: R : arr[R+1]<=x && arr[R]>=x && R>=0 && R<=len-2
    public static int binarySearchIterative(int[] arr, int x, int len) {
        int left = 0;
        int right = len-1;
        //Inv:arr[right]<=x && arr[left]>=x && right>left && left>=0 && right<=len-1
        while (right - left > 1) {
            //arr[right]<=x && arr[left]>=x && left>=0 && right<=len-1 && right > left + 1
            int mid = (left + right) / 2;
            // right > left + 1 --> mid > left && mid < right -->
            //Pred: arr[right]<=x && arr[left]>=x && right>mid>left && left>=0 && right<=len-1
            if (x >= arr[mid]) {
                //arr[right]<=x && arr[left]>=x && right>mid>left && x >= arr[mid] && left>=0 && mid<len-1
                //arr[mid]<=x && arr[left]>=x && right>mid>left && left>=0 && mid<len-1
                right = mid;
                //arr[right]<=x && arr[left]>=x && right>left && left>=0 && right<len-1
            } else {
                //arr[right]<=x && arr[left]>=x && right>mid>left && x < arr[mid] && left>=0 && right<=len-1
                //arr[right]<=x && arr[mid]>x && right>mid>left && mid>0 && right<=len-1
                left = mid;
                //arr[right]<=x && arr[left]>x && right>left && left>0 && right<=len-1
            }
            //Post:arr[right]<=x && arr[left]>=x && right>left && left>=0 && right<=len-1
        }
        //Post:arr[right]<=x && arr[left]>=x && right-left>0  && right - left <=1 && left>=0 && right<=len-1
        //PostSimplified1:arr[right]<=x && arr[left]>=x && right-left ==1 && left>=0 && right<=len-1
        //PostSimplified2:arr[right]<=x && arr[right-1]>=x && right>=1 && right<=len-1
        return right-1;
        //R : arr[R+1]<=x && arr[R]>=x && R>=0 && R<=len-2
    }

    //NotaBene: len = arr.length && len>=2
    //Pred: forall i=0..len-2 arr[i]>=arr[i+1] && x = int && left >= 0 &&
    //right <= len - 1 && right > left && arr[right]<=x && arr[left]>=x
    //Post: R : arr[R+1]<=x && arr[R]>=x && R>=0 && R<=len-2
    public static int binarySearchRecursive(int[] arr, int x, int left, int right) {
        //arr[right]<=x && arr[left]>=x && right>left && left>=0 && right<=len-1
        if (right - left <= 1) {
            //Post:arr[right]<=x && arr[left]>=x && right>left && right - left <=1 && left>=0 && right<=len-1
            //PostSimplified1:arr[right]<=x && arr[left]>=x && right - left ==1 && left>=0 && right<=len-1
            //PostSimplified2:arr[right]<=x && arr[right-1]>=x && right>=1 && right<=len-1
            return right - 1;
            //arr[R+1]<=x && arr[R]>=x && R>=0 && R<=len-2
        }
        //arr[right]<=x && arr[left]>=x && left>=0 && right<=len && right > left + 1 && left>=0 && right<=len-1
        int mid = (left + right) / 2;
        // mid > left && mid < right -->
        //Pred: arr[right]<=x && arr[left]>=x && right>mid>left && left>=0 && right<=len-1
        if (x >= arr[mid]) {
            //arr[right]<=x && arr[left]>=x && right>mid>left && x >= arr[mid] && left>=0 && mid<len-1
            //Pred:arr[mid]<=x && arr[left]>x && mid>left && left>=0 && mid<len-1 - correct
            return binarySearchRecursive(arr, x, left, mid);
        } else {
            //arr[right]<=x && arr[left]>x && right>mid>left && x < arr[mid] && left>=0 && right<=len-1
            //Pred:arr[right]<=x && arr[mid]>x && right>mid && mid>0 && right<=len-1 - correct
            return binarySearchRecursive(arr, x, mid, right);
        }
        //Post:arr[right]<=x && arr[left]>=x && right-left>0  && right - left <=1 && left>=0 && right<=len-1
    }
}
