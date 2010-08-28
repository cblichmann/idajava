// Modified code from http://www.codeproject.com/KB/tips/va_pass.aspx

template<int count> struct SVaPassNext {
    SVaPassNext<count - 1> big;
    unsigned long dw;
};
template<> struct SVaPassNext<0> {};

// SVaPassNext - is generator of structure of any size at compile time.
class CVaPassNext {
public:
	// Allocate 50 times sizeof(unsigned long) bytes
	SVaPassNext<50> svapassnext;
	CVaPassNext(va_list &args) {
		// Avoid access violation
		try {
			memcpy(&svapassnext, args, sizeof(svapassnext));
		} catch (...) { /* Eat */ }
	}
};

#define va_pass(valist) CVaPassNext(valist).svapassnext
