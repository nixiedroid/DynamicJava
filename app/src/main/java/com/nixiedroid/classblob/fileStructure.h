/*
* Description of file, containing multiple java .class files
* max file size
*
*/

struct blobHeader { //96 bytes
    uint32_t magic; //=0xCAFEB10B
    uint16_t minor_ver; //1 LITTLE_ENDIAN
    uint16_t major_ver; //1 LITTLE_ENDIAN
    uint32_t classes_amount; //LITTLE_ENDIAN
    classDesc[] class_headers;
    classData[] classes
};
//OPTIONAL. Before all class headers
//
struct bootStrapClass{
    uint32_t class_flags; //LITTLE_ENDIAN. Exactly 0x22
    uint32_t offset; //from start of file  LITTLE_ENDIAN
    uint32_t class_File_size; //LITTLE
    uint16_t classNameLen;  //LITTLE
    uint8_t[] className //ASCII encoded, arr[0]==begin
}
struct classHeader{ //96 bytes
    uint32_t class_flags; //LITTLE_ENDIAN
    uint32_t offset; //from start of file LITTLE_ENDIAN
    uint32_t class_File_size;//LITTLE
    //OPTIONAL if NAME_PROVIDED flag is set
    uint16_t classNameLen;  //LITTLE
    //OPTIONAL if NAME_PROVIDED flag is set
    uint8_t[] className; //ASCII encoded,arr[0]==begin
     //OPTIONAL if ENCRYPTED or DECRYPTOR flag is set
    uint32_t algId //LITTLE
};
struct classData{
    uint8_t[] classBytes //arr[0]==begin
};


