import sys
import re
from textblob import TextBlob
from collections import Counter

n=6000
train_n = 5000
test_n = 1000
allwords = re.findall('\w+',open(sys.argv[1]).read())
word_list = Counter(allwords).most_common(n)


m = open(sys.argv[6],"r")
tags={}
for line in m:
	pair = line.split('\t')
	tags[pair[0]] = pair[1].rstrip()
m.close()

f1= open(sys.argv[2],"w")
f2 = open(sys.argv[3],"w")
source = sys.argv[4]
target =sys.argv[5]
count = 0
for word in word_list:
	word_map = TextBlob(word[0]).translate(from_lang=source,to=target)
	#tag = tags[TextBlob(word[0]).tags[0][1]]
	word_pair = (word[0].rstrip() + " " + word_map.string + "\n")
	count = count + 1
	if count <= train_n:
		f1.write(word_pair.encode('utf8'))
	else :
		f2.write(word_pair.encode('utf8'))
	
	

f1.close()
f2.close()
	
