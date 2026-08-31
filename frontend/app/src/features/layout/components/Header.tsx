import { useEffect, useRef, useState } from "react";

type HeaderProps = {
  onMenuClick?: () => void;
};

export function Header({ onMenuClick }: HeaderProps) {
  const [visible, setVisible] = useState(true);
  const prevScrollPos = useRef(0);
  const scrollUpAnchor = useRef(0);
  const SCROLL_UP_THRESHOLD = 50;

  useEffect(() => {
    const handleScroll = () => {
      const currentScrollPos = window.scrollY;

      if (currentScrollPos < SCROLL_UP_THRESHOLD) {
        setVisible(true);
        prevScrollPos.current = currentScrollPos;
        return;
      }

      const isScrollingDown = currentScrollPos > prevScrollPos.current;

      if (isScrollingDown) {
        setVisible(false);
        scrollUpAnchor.current = currentScrollPos;
      } else {
        const distanceScrolledUp = scrollUpAnchor.current - currentScrollPos;

        if (distanceScrolledUp > SCROLL_UP_THRESHOLD) {
          setVisible(true);
        }
      }

      prevScrollPos.current = currentScrollPos;
    };

    window.addEventListener("scroll", handleScroll, { passive: true });
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  return (
    <header
      className={`sticky mx-auto w-full max-w-6xl top-0 z-30 px-4 pt-4 transition-transform duration-300 ease-in-out ${
        visible ? "translate-y-0" : "-translate-y-full"
      }`}
    >
      <div className="mx-auto flex items-center justify-between gap-3 rounded-full border border-white/40 bg-white/30 px-4 py-3 shadow-[0_8px_32px_0_rgba(31,38,135,0.06)] backdrop-blur-xl transition-all duration-300 dark:border-white/10 dark:bg-slate-950/40 md:px-6">
        <div className="flex min-w-0 flex-1 items-center gap-3 md:gap-0">
          {onMenuClick && (
            <button
              type="button"
              onClick={onMenuClick}
              aria-label="Otwórz menu"
              className="inline-flex shrink-0 items-center justify-center rounded-full p-2 text-slate-700 transition-colors hover:bg-white/30 hover:text-slate-900 md:hidden dark:text-slate-200 dark:hover:bg-slate-800/40 dark:hover:text-white"
            >
              <MenuIcon />
            </button>
          )}

          <nav className="hidden md:flex items-center space-x-8 text-sm font-medium text-slate-700 dark:text-slate-200">
            <a href="#" className="hover:text-blue-600 dark:hover:text-blue-400 transition-colors">
              Start
            </a>
            <a href="#" className="hover:text-blue-600 dark:hover:text-blue-400 transition-colors">
              Funkcje
            </a>
            <a href="#" className="hover:text-blue-600 dark:hover:text-blue-400 transition-colors">
              Cennik
            </a>
            <a href="#" className="hover:text-blue-600 dark:hover:text-blue-400 transition-colors">
              O nas
            </a>
          </nav>
        </div>

        <div className="shrink-0">
          <a
            href="#"
            className="inline-flex items-center justify-center bg-slate-900 text-white dark:bg-white dark:text-slate-900 px-5 py-2.5 rounded-full text-sm font-semibold hover:bg-slate-800 dark:hover:bg-slate-100 transition-all shadow-sm"
          >
            Zacznij teraz
          </a>
        </div>
      </div>
    </header>
  );
}

function MenuIcon() {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      className="h-5 w-5"
      aria-hidden
    >
      <path strokeLinecap="round" d="M4 7h16M4 12h16M4 17h16" />
    </svg>
  );
}
