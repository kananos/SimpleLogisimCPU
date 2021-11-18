// init snake, Mem 1->x(4), Mem 2->y(4)

movi r0, 1
movi r1, 4
sw r1, 0(r0)
sw r1, 1(r0)

// right move snake
// load snake x-axis
lw r1, 0(r0)
// x = x + 1, set to ram
addi r1, r1, 1
sw r1, 0(r0)