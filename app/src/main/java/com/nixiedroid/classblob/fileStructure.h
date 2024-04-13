/*
* Description of file, containing multiple java .class files
* max file size
*
*/

struct classBlob { //96 bytes
  uint32_t magic; //=0xCAFEB10B
  uint16_t minor_ver; //1 LITTLE_ENDIAN
  uint16_t major_ver; //1 LITTLE_ENDIAN
  uint32_t classes_amount; //1 LITTLE_ENDIAN
  classDesc[] class_headers;
  classData[] classes
};
//OPTIONAL. Before all class headers
//
struct bootStrapClass{
     uint32_t class_flags; //LITTLE_ENDIAN
      uint32_t offset; //from end of this block LITTLE_ENDIAN
      uint32_t class_File_size; //LITTLE
      uint8_t[] className //ASCII encoded, NULL-terminated string. arr[0]==begin
}
struct classDesc { //96 bytes
  uint32_t class_flags; //LITTLE_ENDIAN
  uint32_t offset; //from end of this block LITTLE_ENDIAN
  uint32_t class_File_size; //LITTLE
};
struct classData{
  uint8_t[] classBytes //arr[0]==begin
};


