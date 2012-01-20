\clearpage
\section{Decisions}
\label{sec:decisions}

$project.decisions:{ decision |
\subsection{$decision.name$}

\begin{description}
    \item[State] $decision.currentVersion.state.statusName$ 
    \item[Initiators]
        \begin{itemize}
$decision.currentVersion.initiators:{ member |            \item $member.person.name$
}$        \end{itemize}
    \item[Decided when] $decision.currentVersion.decidedWhen;format="yyyy-MM-dd hh:mm"$
    \item[Documented when] $decision.currentVersion.documentedWhen;format="yyyy-MM-dd hh:mm"$
    \item[Template] $decision.template.name$
\end{description}

$decision.values:{ value |
\subsubsection{$value.component.label$}
$value.value$

}$

$if(decision.currentVersion.concerns)$
\subsubsection{Concerns}
\begin{itemize}
$decision.currentVersion.concerns:{ concern |     \item $concern.name$
}$\end{itemize}
$endif$

$if(decision.currentVersion.outgoingRelationships)$
\subsubsection{Outgoing relationships}
\begin{itemize}
$decision.currentVersion.outgoingRelationships:{ rel |     \item $rel.target.decision.name$ \textless{}$rel.type.name$\textgreater{}
}$\end{itemize}
$endif$

$if(decision.currentVersion.incomingRelationships)$
\subsubsection{Incoming relationships}
\begin{itemize}
$decision.currentVersion.incomingRelationships:{ rel |     \item $rel.source.decision.name$ \textless{}$rel.type.name$\textgreater{}
}$\end{itemize}
$endif$

\subsubsection{Version history}
\begin{itemize}
$decision.versions:{ version |     \item $version.state.statusName$ \textless{}$version.decidedWhen;format="yyyy-MM-dd hh:mm"$\textgreater{}
}$\end{itemize}
}$