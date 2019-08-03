# maven groupId
#read -p "input group id" groupId
# projectName
#read -p "input project name" projectName
# 当前目录
dir=$(pwd)
echo "当前目录${dir}"
# 从git下载模板项目
git clone https://github.com/dai-shun/spring-examples.git spring-examples
# 删除单文件
cp -r ${dir}/spring-examples/spring-mybatis-simple-example/* ${dir}
rm -rf ${dir}/spring-examples


#dir = './spring-mybatis-simple-example'
example='example-model'
replacement='mybatis-plugins-example-model'
projectName='mybatis-plugins-example'
function getDir(){
    for element in `ls $1`
      do
        dir_or_file=$1"/"$element
    if [ -d $dir_or_file ]
      then
        getDir $dir_or_file
      else
        replaceModel $dir_or_file
#        echo ${dir_or_file//example/replacement}
    fi
done
}
function replaceModel(){
    absolutePath=$1
    newModelName="${projectName}-model"
    filePath=${absolutePath//example-model/${modelName}}
    mv $absolutePath $filePath
    echo $filePath
}
getDir $dir
