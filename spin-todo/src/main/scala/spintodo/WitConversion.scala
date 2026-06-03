package spintodo

import java.util.Optional

import scala.language.implicitConversions
import scala.scalajs.{wit => wm}

object WitConversion {
  implicit def resultToEither[A, E](result: wm.Result[A, E]): Either[E, A] = result match {
    case ok: wm.Ok[_]  => Right(ok.value.asInstanceOf[A])
    case err: wm.Err[_] => Left(err.value.asInstanceOf[E])
  }

  implicit def optionalToOption[A](optional: Optional[A]): Option[A] =
    if (optional.isPresent()) Some(optional.get())
    else None
}
