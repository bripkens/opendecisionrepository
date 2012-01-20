\clearpage
\section{Iterations}
\label{sec:iterations}

$project.iterations:{ iteration |
\subsection{$iteration.name$}

\begin{description}
    \item[Documentation date] $iteration.documentedWhen;format="yyyy-MM-dd hh:mm"$
    \item[Start date] $iteration.startDate;format="yyyy-MM-dd hh:mm"$
    \item[End date] $iteration.endDate;format="yyyy-MM-dd hh:mm"$
\end{description}

$iteration.description$
}$
