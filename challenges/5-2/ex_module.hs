module Example
where

  import Prelude hiding (max, signum)

  max :: Ord a => a -> a -> a
  max x y
    | x < y = y
    | otherwise = x

  signum :: (Num a, Ord a) => a -> Int
  signum x
    | x < 0 = -1
    | x == 0 = 0
    | otherwise = 1
  