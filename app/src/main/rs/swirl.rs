#pragma version(1)
#pragma rs java_package_name(com.imagewarp.andy.imagewarp)

uchar4 __attribute__((kernel)) bar(uint32_t x, uint32_t y) {
    uchar4 ret = ((uchar) x, (uchar) y, (uchar) (x + y), (uchar)255);
    return ret;
}