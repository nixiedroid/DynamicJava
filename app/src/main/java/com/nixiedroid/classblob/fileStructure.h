/*
* Description of file, containing multiple java .class files
* max file size
*
*/

struct classBlob {
  uint32_t magic; //=0xCAFEB10B
  uint16_t minor_ver; //1 LITTLE_ENDIAN
  uint16_t major_ver; //1 LITTLE_ENDIAN
  classDesc[] class_headers; //64 bytes
  classData[] classes
};
struct classDesc {
  uint32_t class_flags; //LITTLE_ENDIAN
  uint32_t offset //from start of file LITTLE_ENDIAN
};
struct classData{
  uint8_t[] classBytes;
};


