x=0
for file in $(find java/ -name "*.java"); do
    a=$(cat "$file" | wc -l)
    x=$(($x + $a))
done
echo "$x"
