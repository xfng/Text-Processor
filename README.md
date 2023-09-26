# TextProcessor

TextProcessor is a Java command-line tool for performing various text processing operations on a text file. It provides several options to manipulate and analyze text data according to your needs.

Download textprocessor.jar

## Usage
```
java -jar textprocessor.jar [ -o filename | -i | -k substring | -r old new | -n padding | -w | -s suffix ] FILE
```
## Options
- `-i` : Specify the input file (required).
- `-o` : Specify the output file.
- `-k` : Keep only the lines containing substring. The search for substring is case-sensitive, unless option -i is set. 
- `-r` : Replaces the first instance of string old in each line with string new.
- `-n` : Add a line number followed by a single space to the beginning of each line, where padding is an integer in the inclusive range of 1 to 9 specifying the minimum padding of the line number field. If the number of digits in the line number is less than the specified padding, zeros are left padded until the minimum padding is reached. If the number of digits in the line number is greater than the specified padding, the line number is never truncated. Line numbering should start at 1.
- `-w` : Removes all whitespace from lines.
- `-s` : Adds the string suffix at the end of each line.

## Examples

**Example 1:**
textprocessor -o sample.txt FILE
input FILE:
This is the first line of the input file.↵

output sample.txt: 
This is the first line of the input file.↵

stdout: nothing sent to stdout
stderr: nothing sent to stderr

**Example 2:**
textprocessor -r 02 two FILE
input FILE:
Some words are: "one", "02", and "three"↵

output file: output file not created
stdout: 
Some words are: "one", "two", and "three"↵

stderr: nothing sent to stderr

**Example 3:**
textprocessor -i -r the A FILE
input FILE:
The file↵
the file↵

output file: output file not created
stdout: 
A file↵
A file↵

stderr: nothing sent to stderr

**Example 4:**
textprocessor -s er FILE
input FILE:
This is cool↵

output file: output file not created
stdout: 
This is cooler↵

stderr: nothing sent to stderr

**Example 5:**
textprocessor -k Java FILE
input FILE:
java is one of the <blank> programming languages.↵
Java is a programming language.↵
Programming languages are neat, an example of one is Java.↵
  
output file: output file not created
stdout: 
Java is a programming language.↵
Programming languages are neat, an example of one is Java.↵

stderr: nothing sent to stderr

**Example 6:**
textprocessor -r Question Exclamation -o text -s ! -w FILE
input FILE:
This Sentence Ends In A Question Mark?↵
  
output text:
ThisSentenceEndsInAExclamationMark?!↵

stdout: nothing sent to stdout
stderr: nothing sent to stderr

**Example 7:**
textprocessor -n 8 -n 2 -s ## –s ! FILE
input FILE:
I wish this line had a line number..↵
I also wish that..↵
  
output file: output file not created
stdout: 
01 I wish this line had a line number..!↵
02 I also wish that..!↵

stderr: nothing sent to stderr

**Example 8:**
textprocessor
input FILE:
Today is January 65, 2298.↵
Yesterday was December 0, 3000.↵
Tomorrow we will time travel again.
  
output file: output file not created
stdout: nothing sent to stdout
stderr: 
Usage: textprocessor [ -o filename | -i | -k substring | -r old new | -n padding | -w | -s suffix ] FILE

**Example 9:**
textprocessor -i -k #keep -n 1 -s # FILE
input FILE:
This course's title is CS6300. #keep↵
CS stands for Counter Strike.↵
It is part of the OMSCS program. #KEEP↵
  
output file: output file not created
stdout: 
1 This course's title is CS6300. #keep#↵
3 It is part of the OMSCS program. #KEEP#↵

stderr: nothing sent to stderr

**Example 10:**
textprocessor -r -k -s FILE
input FILE:
This list contains words that start with -k:↵
-kale↵
-kilo↵
-kite↵
- knot↵
  
output file: output file not created
stdout:
This list contains words that start with -s:↵
-sale↵
-silo↵
-site↵
- knot↵

stderr: nothing sent to stderr