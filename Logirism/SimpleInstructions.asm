// r0 save the base-address
movi r0, 2

movi r1, 5
sw r1, 0(r0)
addi r1, r1, 2
sw r1, 1(r0)
// x = 5, y = 7

lw r1, 0(r0) 
// x = x + 1, set to ram
addi r1, r1, 1
sw r1, 0(r0)

lw r1, 1(r0)
// y = y - 4
subi r1, r1, 4
sw r1, 1(r0)

