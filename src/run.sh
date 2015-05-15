#!/bin/sh

f1=$(basename "$1")
f2=$(basename "$2")


##preprocessing
./preprocessing/preprocess.sh $1 data/preprocessed/$f1"_2"
python preprocessing/ascii.py data/preprocessed/$f1"_2" > data/preprocessed/$f1
./preprocessing/preprocess.sh data/preprocessed/$f1 data/preprocessed/$f1"_final"

./preprocessing/preprocess.sh $2 data/preprocessed/$f2"_2"
python preprocessing/ascii.py data/preprocessed/$f2"_2" > data/preprocessed/$f2
./preprocessing/preprocess.sh data/preprocessed/$f2 data/preprocessed/$f2"_final"

#python preprocessing/ascii.py $2 > data/preprocessed/$f2
#./preprocessing/preprocess.sh data/preprocessed/$f2

##tagging
cd tagger
./gradlew executeTaggerMain -Psource=../data/preprocessed/$f1"_final" -Poutput=../data/tagged/$f1"_output" -Planguage=$3
./gradlew executeTaggerMain -Psource=../data/preprocessed/$f2"_final" -Poutput=../data/tagged/$f2"_output" -Planguage=$4
cd ..

#vector
./word2vec/svn/trunk/word2vec -train /u/ankitade/nlp_project/data/tagged/$f1"_output" -output /u/ankitade/nlp_project/data/vectors/$f1"_$5_vec.txt" -cbow 1 -size $5 -window 8 -negative 25 -hs 0 -sample 1e-4 -threads 20 -iter 15

./word2vec/svn/trunk/word2vec -train /u/ankitade/nlp_project/data/tagged/$f2"_output" -output /u/ankitade/nlp_project/data/vectors/$f2"_$6_vec.txt" -cbow 1 -size $6 -window 8 -negative 25 -hs 0 -sample 1e-4 -threads 20 -iter 15

#./word2vec/svn/trunk/word2vec -train /u/ankitade/nlp_project/data/preprocessed/$f1"_data_final" -output /u/ankitade/nlp_project/data/vectors/$f1"_withoutpos_$5_vec.txt" -cbow 1 -size $5 -window 8 -negative 25 -hs 0 -sample 1e-4 -threads 20 -iter 15

#./word2vec/svn/trunk/word2vec -train /u/ankitade/nlp_project/data/preprocessed/$f2"_data_final" -output /u/ankitade/nlp_project/data/vectors/$f2"_withoutpos_$6_vec.txt" -cbow 1 -size $6 -window 8 -negative 25 -hs 0 -sample 1e-4 -threads 20 -iter 15

dictionary
python dictionary/Create_dictionary.py $1 ./data/dictionary/train/$f1"_$3_$4_train.txt" ./data/dictionary/test/$f1"_$3_$4_test.txt" en es dictionary/en-universal-pos.map 10 6 


#translation
python translation/train_tm.py -o /u/ankitade/nlp_project/data/translation_matrix/tm_$f1"_$3_$4_$5_$6" /u/ankitade/nlp_project/data/dictionary/train/$f1"_$3_$4_train.txt" /u/ankitade/nlp_project/data/vectors/$f1"_$5_vec.txt" /u/ankitade/nlp_project/data/vectors/$f2"_$6_vec.txt"

python translation/test_tm.py /u/ankitade/nlp_project/data/translation_matrix/tm_$f1"_$3_$4_$5_$6.txt" /u/ankitade/nlp_project/data/dictionary/test/$f1"_$3_$4_test.txt" /u/ankitade/nlp_project/data/vectors/$f1"_$5_vec.txt" /u/ankitade/nlp_project/data/vectors/$f2"_$6_vec.txt" > /u/ankitade/nlp_project/results/withpos/$f1"_$3_$4_$5_$6.txt"

#python translation/train_tm.py -o /u/ankitade/nlp_project/data/translation_matrix/tm_$f1"_withoutpos_$3_$4_$5_$6" /u/ankitade/nlp_project/data/dictionary/train/$f1"_withoutpos_$3_$4_train.txt" /u/ankitade/nlp_project/data/vectors/$f1"_withoutpos_$5_vec.txt" /u/ankitade/nlp_project/data/vectors/$f2"_withoutpos_$6_vec.txt"

#python translation/test_tm.py /u/ankitade/nlp_project/data/translation_matrix/tm_$f1"_withoutpos_$3_$4_$5_$6.txt" /u/ankitade/nlp_project/data/dictionary/test/$f1"_withoutpos_$3_$4_test.txt" /u/ankitade/nlp_project/data/vectors/$f1"_withoutpos_$5_vec.txt" /u/ankitade/nlp_project/data/vectors/$f2"_withoutpos_$6_vec.txt" > /u/ankitade/nlp_project/results/withoutpos/$f1"_$3_$4_$5_$6.txt"
