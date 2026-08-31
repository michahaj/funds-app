export function ColorBlobs() {
  return (
    <div className="absolute top-0 left-0 right-0 h-[600px] overflow-hidden pointer-events-none z-0 select-none opacity-70">
      <div className="absolute -top-40 left-[10%] w-[400px] h-[400px] rounded-full bg-purple-600/30 blur-[120px]" />
      <div className="absolute -top-20 right-[15%] w-[500px] h-[400px] rounded-full bg-cyan-500/20 blur-[130px]" />
    </div>
  );
}
