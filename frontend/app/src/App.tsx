export default function App() {
  const maslo = () => {
    void fetch("http://localhost:8080/api/hello");
  };
  return (
    <div>
      <h1>Witaj w FinTech App (React + Vite+) 🚀</h1>
      <p>Środowisko jest w pełni gotowe do pracy!</p>
      <button onClick={maslo}>Masło</button>
    </div>
  );
}
