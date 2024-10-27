;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-advanced-reader.ss" "lang")((modname empty) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #t #t none #f () #f)))

(define-struct book (title author publisher price))

(define B1 (make-book "ss" "ss" "ss" 12))
(define B2 (make-book "ss" "ss" "ss" 46))
(define B3 (make-book "ss" "ss" "ss" 21))

(define LOB0 empty)
(define LOB1 (list B1))
(define LOB2(list B2 B1))
(define LOB3 (list B3 B2 B1))

(define (total-price lob)
  (foldr (λ (b acc) (+ (book-price b) acc)) 0 lob)
  )

(check-expect (total-price LOB0) 0)
(check-expect (total-price LOB1) 12)
(check-expect (total-price LOB2) 58)
(check-expect (total-price LOB3) 79)