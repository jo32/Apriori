Apriori
=======
ENVIRONMENT:

1. Java Runtime Environment (coded on Java 7)

USAGE:

1. enter the directory of the file "apriori.jar"
2. execute command: java -jar apriori.jar
3. when encounter hint "Please input the file path (absolute path):", enter: /absolute/path/to/the/file/asso.csv
4. wait for result

ATTENTION:

1. This app is designed for the "asso.csv" file. Namely, the storage, memory, etc. considerations are for the "asso.csv" file, you cannot count it on other file. 

FEATURE OF THIS IMPLEMENTATION:

1. largest common substring used in counting support is implemented in dynamic programming to accelerate the speed. (One time IO, One time scan.)
2. pruning in getting item sets and rules are strictly and efficiently implemented, you can check out on the source code.
3. all cached variables are sorted and binary seached.
4. hardly no brute force method used unless the case of input is significantly small (Getting more space is slowing down the speed).
5. object-oriented designed, codes are easy to read.