import sys 
reload(sys) 
sys.setdefaultencoding("utf-8")
import csv
import unicodedata

#def remove_accents(input_str):
#    nkfd_form = unicodedata.normalize('NFKD', unicode(input_str))
#    return u"".join([c for c in nkfd_form if not unicodedata.combining(c)])

f=open(sys.argv[1],"r")
for line in f:
	print(line.replace("\xc2\xa0", " "))


