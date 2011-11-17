\clearpage
\section{Concerns}
\label{sec:concerns}

$project.destinctConcerns:{ concern |
\subsection{$concern.name$}

$if(concern.tags)$
\begin{itemize}
$concern.tags:{ tag | \item $tag$
}$\end{itemize}
$endif$

$concern.description$
}$
